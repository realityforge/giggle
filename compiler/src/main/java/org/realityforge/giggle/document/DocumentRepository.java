package org.realityforge.giggle.document;

import graphql.language.Document;
import graphql.language.Field;
import graphql.language.OperationDefinition;
import graphql.language.SelectionSet;
import graphql.parser.Parser;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.validation.ValidationError;
import graphql.validation.ValidationErrorType;
import graphql.validation.Validator;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.annotation.Nonnull;
import org.realityforge.giggle.util.GraphQLUtil;

/**
 * Container responsible for building GraphQL documents.
 */
public final class DocumentRepository
{
  @Nonnull
  private final Parser _parser = new Parser();
  @Nonnull
  private final Validator _validator = new Validator();

  /**
   * Return a GraphQL Document instance constructed from the supplied files.
   *
   * @param schema     the schema against which the document is validate.
   * @param components the file components that are used to construct the document.
   * @return a GraphQL Document instance.
   * @throws DocumentValidateException if the document can not be correctly parsed or validated.
   * @throws DocumentReadException     if there is an error reading a file component.
   */
  @Nonnull
  public Document getDocument( @Nonnull final GraphQLSchema schema, @Nonnull final List<Path> components )
    throws DocumentReadException, DocumentValidateException
  {
    assert !components.isEmpty();
    Document document = Document.newDocument().build();
    for ( final Path component : components )
    {
      document = document.transform( b -> mergeComponent( b, component ) );
    }
    final List<ValidationError> errors = _validator.validateDocument( schema, document );
    validateNoAnonymousOperations( document, errors );
    validateTopLevelFieldsMatchOperationType( schema, document, errors );
    if ( errors.isEmpty() )
    {
      return document;
    }
    else
    {
      throw new DocumentValidateException( errors );
    }
  }

  private void validateNoAnonymousOperations( @Nonnull final Document document,
                                              @Nonnull final List<ValidationError> errors )
  {
    document.getDefinitionsOfType( OperationDefinition.class )
      .stream()
      .filter( d -> null == d.getName() )
      .findAny()
      .ifPresent( definition -> errors.add( new ValidationError( ValidationErrorType.LoneAnonymousOperationViolation,
                                                                 definition.getSourceLocation(),
                                                                 "Giggle does not allow anonymous operations." ) ) );
  }

  private void validateTopLevelFieldsMatchOperationType( @Nonnull final GraphQLSchema schema,
                                                         @Nonnull final Document document,
                                                         @Nonnull final List<ValidationError> errors )
  {
    // TODO(graphql-java/graphql-java#1642): The toolkit should flag the scenario where invalid top-level fields
    //  are present but does not at the moment so we perform the validation.
    for ( final OperationDefinition definition : document.getDefinitionsOfType( OperationDefinition.class ) )
    {
      final SelectionSet selectionSet = definition.getSelectionSet();
      final List<Field> fields = selectionSet.getSelectionsOfType( Field.class );
      for ( final Field field : fields )
      {
        final String topLevelFieldName = GraphQLUtil.getTopLevelFieldName( definition.getOperation() );
        final GraphQLObjectType query = schema.getObjectType( topLevelFieldName );
        final GraphQLFieldDefinition fieldDefinition =
          null == query ? null : query.getFieldDefinition( field.getName() );
        if ( null == fieldDefinition )
        {
          final String message =
            String.format( "Field '%s' of type '%s' is undefined", field.getName(), topLevelFieldName );
          errors.add( new ValidationError( ValidationErrorType.FieldUndefined, field.getSourceLocation(), message ) );
        }
      }
    }
  }

  private void mergeComponent( @Nonnull final Document.Builder builder, @Nonnull final Path component )
  {
    _parser
      .parseDocument( readDocument( component ), component.toString() )
      .getDefinitions()
      .forEach( builder::definition );
  }

  @Nonnull
  private String readDocument( @Nonnull final Path component )
  {
    try
    {
      return new String( Files.readAllBytes( component ), StandardCharsets.UTF_8 );
    }
    catch ( final IOException ioe )
    {
      throw new DocumentReadException( "Error reading file " + component, ioe );
    }
  }
}
