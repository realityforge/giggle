package org.realityforge.giggle;

import graphql.schema.GraphQLSchema;
import graphql.schema.idl.errors.SchemaProblem;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import org.realityforge.getopt4j.CLArgsParser;
import org.realityforge.getopt4j.CLOption;
import org.realityforge.getopt4j.CLOptionDescriptor;
import org.realityforge.getopt4j.CLUtil;
import org.realityforge.giggle.schema.SchemaRepository;

/**
 * The entry point in which to run the tool.
 */
public class Main
{
  private static final int HELP_OPT = 'h';
  private static final int QUIET_OPT = 'q';
  private static final int VERBOSE_OPT = 'v';
  private static final int SCHEMA_FILE_OPT = 's';
  private static final int DOCUMENT_FILE_OPT = 'd';
  private static final CLOptionDescriptor[] OPTIONS = new CLOptionDescriptor[]
    {
      new CLOptionDescriptor( "help",
                              CLOptionDescriptor.ARGUMENT_DISALLOWED,
                              HELP_OPT,
                              "print this message and exit" ),
      new CLOptionDescriptor( "quiet",
                              CLOptionDescriptor.ARGUMENT_DISALLOWED,
                              QUIET_OPT,
                              "Do not output unless an error occurs.",
                              new int[]{ VERBOSE_OPT } ),
      new CLOptionDescriptor( "verbose",
                              CLOptionDescriptor.ARGUMENT_DISALLOWED,
                              VERBOSE_OPT,
                              "Verbose output of differences.",
                              new int[]{ QUIET_OPT } ),
      new CLOptionDescriptor( "schema",
                              CLOptionDescriptor.ARGUMENT_REQUIRED | CLOptionDescriptor.DUPLICATES_ALLOWED,
                              SCHEMA_FILE_OPT,
                              "The path to a graphql schema file." ),
      new CLOptionDescriptor( "document",
                              CLOptionDescriptor.ARGUMENT_REQUIRED | CLOptionDescriptor.DUPLICATES_ALLOWED,
                              DOCUMENT_FILE_OPT,
                              "The path to a graphql document file." )
    };
  private static final Environment c_environment =
    new Environment( System.console(), Paths.get( "" ).toAbsolutePath(), Logger.getGlobal() );

  public static void main( final String[] args )
  {
    setupLogger();
    if ( !processOptions( c_environment, args ) )
    {
      System.exit( ExitCodes.ERROR_PARSING_ARGS_EXIT_CODE );
      return;
    }

    final Logger logger = c_environment.logger();
    try
    {
      final SchemaRepository schemaRepository = new SchemaRepository();
      final GraphQLSchema schema = schemaRepository.getSchema( c_environment.getSchemaFiles() );

    }
    catch ( final SchemaProblem sp )
    {
      logger.log( Level.WARNING, "Error: Failed to parse schema" );
      final List<GraphQLError> errors = sp.getErrors();
      for ( final GraphQLError error : errors )
      {
        final String locations =
          error.getLocations()
            .stream()
            .map( e -> e.getSourceName() + ":" + e.getLine() )
            .collect( Collectors.joining( " " ) );
        logger.log( Level.WARNING, error.getErrorType() + ":" + error.getMessage() +
                                   ( locations.isEmpty() ? "" : " @ " + locations ) );
      }

      System.exit( ExitCodes.ERROR_PARSING_SCHEMA_EXIT_CODE );
    }
    catch ( final TerminalStateException tse )
    {
      final String message = tse.getMessage();
      if ( null != message )
      {
        logger.log( Level.WARNING, message );
        final Throwable cause = tse.getCause();
        if ( null != cause )
        {
          if ( logger.isLoggable( Level.INFO ) )
          {
            logger.log( Level.INFO, "Cause: " + cause.toString() );
            if ( logger.isLoggable( Level.FINE ) )
            {
              cause.printStackTrace();
            }
          }
        }
      }
      System.exit( tse.getExitCode() );
    }
    catch ( final Throwable t )
    {
      logger.log( Level.WARNING, t.toString(), t );
      System.exit( ExitCodes.ERROR_EXIT_CODE );
    }

    System.exit( ExitCodes.SUCCESS_EXIT_CODE );
  }

  private static void setupLogger()
  {
    final ConsoleHandler handler = new ConsoleHandler();
    handler.setFormatter( new RawFormatter() );
    handler.setLevel( Level.ALL );
    final Logger logger = c_environment.logger();
    logger.setUseParentHandlers( false );
    logger.addHandler( handler );
    logger.setLevel( Level.INFO );
  }

  static boolean processOptions( @Nonnull final Environment environment, @Nonnull final String... args )
  {
    // Parse the arguments
    final CLArgsParser parser = new CLArgsParser( args, OPTIONS );

    //Make sure that there was no errors parsing arguments
    final Logger logger = environment.logger();
    if ( null != parser.getErrorString() )
    {
      logger.log( Level.SEVERE, "Error: " + parser.getErrorString() );
      return false;
    }
    // Get a list of parsed options
    final List<CLOption> options = parser.getArguments();
    for ( final CLOption option : options )
    {
      switch ( option.getId() )
      {
        case CLOption.TEXT_ARGUMENT:
        {
          logger.log( Level.SEVERE, "Error: Unknown argument: " + option.getArgument() );
          return false;
        }
        case SCHEMA_FILE_OPT:
        {
          final String argument = option.getArgument();
          final Path schemaFile = environment.currentDirectory().resolve( argument ).toAbsolutePath().normalize();
          if ( !schemaFile.toFile().exists() )
          {
            logger.log( Level.SEVERE,
                        "Error: Specified graphql schema file does not exist. Specified value: " + argument );
            return false;
          }
          environment.addSchemaFile( schemaFile );
          break;
        }
        case DOCUMENT_FILE_OPT:
        {
          final String argument = option.getArgument();
          final Path documentFile = environment.currentDirectory().resolve( argument ).toAbsolutePath().normalize();
          if ( !documentFile.toFile().exists() )
          {
            logger.log( Level.SEVERE,
                        "Error: Specified graphql document file does not exist. Specified value: " + argument );
            return false;
          }
          environment.addDocumentFile( documentFile );
          break;
        }

        case VERBOSE_OPT:
        {
          logger.setLevel( Level.ALL );
          break;
        }
        case QUIET_OPT:
        {
          logger.setLevel( Level.WARNING );
          break;
        }
        case HELP_OPT:
        {
          printUsage( environment );
          return false;
        }
      }
    }

    if ( environment.getSchemaFiles().isEmpty() )
    {
      logger.log( Level.SEVERE, "Error: No schema files specified." );
      return false;
    }

    if ( logger.isLoggable( Level.FINE ) )
    {
      logger.log( Level.FINE, "Giggle Starting..." );
      logger.log( Level.FINE, "  Schema files: " + environment.getSchemaFiles() );
    }

    return true;
  }

  /**
   * Print out a usage statement
   */
  static void printUsage( @Nonnull final Environment environment )
  {
    final Logger logger = environment.logger();
    logger.info( "java " + Main.class.getName() + " [options]" );
    logger.info( "\tOptions:" );
    final String[] options =
      CLUtil.describeOptions( OPTIONS ).toString().split( System.getProperty( "line.separator" ) );
    for ( final String line : options )
    {
      logger.info( line );
    }
  }
}
