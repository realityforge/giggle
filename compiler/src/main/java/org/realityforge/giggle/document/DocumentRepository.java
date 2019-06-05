package org.realityforge.giggle.document;

import graphql.language.Document;
import graphql.language.FragmentDefinition;
import graphql.parser.Parser;
import graphql.schema.GraphQLSchema;
import graphql.validation.ValidationError;
import graphql.validation.ValidationErrorType;
import graphql.validation.Validator;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

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
    validateFragmentNamesUnique( document, errors );
    if ( errors.isEmpty() )
    {
      return document;
    }
    else
    {
      throw new DocumentValidateException( errors );
    }
  }

  private void validateFragmentNamesUnique( @Nonnull final Document document,
                                            @Nonnull final List<ValidationError> errors )
  {
    // TODO(graphql-java/graphql-java#1557): It should be an error if a single document contains multiple
    //  fragments with the same name. graphql-java should verify this but does not at the moment so we
    //  perform the validation.
    final List<FragmentDefinition> fragments = document.getDefinitions()
      .stream()
      .filter( d -> d instanceof FragmentDefinition )
      .map( d -> (FragmentDefinition) d )
      .collect( Collectors.toList() );
    final Map<String, FragmentDefinition> fragmentMap = new HashMap<>();
    for ( final FragmentDefinition fragment : fragments )
    {
      final String name = fragment.getName();
      final FragmentDefinition existing = fragmentMap.get( name );
      if ( null == existing )
      {
        fragmentMap.put( name, fragment );
      }
      else
      {
        errors.add( new ValidationError( ValidationErrorType.FragmentCycle,
                                         Arrays.asList( existing.getSourceLocation(), fragment.getSourceLocation() ),
                                         "Multiple fragments defined with the name '" + name + "'" ) );
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
