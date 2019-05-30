package org.realityforge.giggle.document;

import graphql.validation.ValidationError;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;

public final class DocumentValidateException
  extends RuntimeException
{
  @Nonnull
  private final List<ValidationError> _errors;

  public DocumentValidateException( @Nonnull final List<ValidationError> errors )
  {
    _errors = Objects.requireNonNull( errors );
  }

  @Nonnull
  public List<ValidationError> getErrors()
  {
    return _errors;
  }
}
