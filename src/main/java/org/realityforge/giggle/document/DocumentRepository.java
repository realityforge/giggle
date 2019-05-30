package org.realityforge.giggle.document;

import graphql.language.Document;
import graphql.parser.Parser;
import graphql.schema.GraphQLSchema;
import graphql.validation.ValidationError;
import graphql.validation.Validator;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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
    if ( errors.isEmpty() )
    {
      return document;
    }
    else
    {
      throw new DocumentValidateException( errors );
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
