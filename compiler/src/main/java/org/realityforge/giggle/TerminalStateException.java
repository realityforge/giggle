package org.realityforge.giggle;

import javax.annotation.Nullable;

final class TerminalStateException
  extends RuntimeException
{
  private final int _exitCode;

  TerminalStateException( @Nullable final String message, final int exitCode )
  {
    super( message, null );
    _exitCode = exitCode;
  }

  int getExitCode()
  {
    return _exitCode;
  }
}
