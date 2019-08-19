package org.realityforge.giggle;

import graphql.GraphQLError;
import graphql.language.Document;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.errors.SchemaProblem;
import graphql.validation.ValidationError;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.realityforge.getopt4j.CLArgsParser;
import org.realityforge.getopt4j.CLOption;
import org.realityforge.getopt4j.CLOptionDescriptor;
import org.realityforge.getopt4j.CLUtil;
import org.realityforge.giggle.document.DocumentReadException;
import org.realityforge.giggle.document.DocumentRepository;
import org.realityforge.giggle.document.DocumentValidateException;
import org.realityforge.giggle.generator.GenerateException;
import org.realityforge.giggle.generator.GeneratorContext;
import org.realityforge.giggle.generator.NoSuchGeneratorException;
import org.realityforge.giggle.schema.SchemaReadException;
import org.realityforge.giggle.schema.SchemaRepository;
import org.realityforge.giggle.util.IoUtil;
import org.realityforge.giggle.util.MappingUtil;

/**
 * The entry point in which to run the tool.
 */
public class Main
{
  private static final int HELP_OPT = 'h';
  private static final int QUIET_OPT = 'q';
  private static final int VERBOSE_OPT = 'v';
  private static final int DEFINE_OPT = 'D';
  private static final int SCHEMA_FILE_OPT = 1;
  private static final int DOCUMENT_FILE_OPT = 2;
  private static final int TYPE_MAPPING_FILE_OPT = 3;
  private static final int FRAGMENT_MAPPING_FILE_OPT = 4;
  private static final int OUTPUT_DIRECTORY_OPT = 6;
  private static final int PACKAGE_NAME_OPT = 7;
  private static final int GENERATOR_OPT = 8;
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
                              "The path to a graphql document file." ),
      new CLOptionDescriptor( "type-mapping",
                              CLOptionDescriptor.ARGUMENT_REQUIRED | CLOptionDescriptor.DUPLICATES_ALLOWED,
                              TYPE_MAPPING_FILE_OPT,
                              "The path to a mapping file for types." ),
      new CLOptionDescriptor( "fragment-mapping",
                              CLOptionDescriptor.ARGUMENT_REQUIRED | CLOptionDescriptor.DUPLICATES_ALLOWED,
                              FRAGMENT_MAPPING_FILE_OPT,
                              "The path to a mapping file for fragments." ),
      new CLOptionDescriptor( "define",
                              CLOptionDescriptor.ARGUMENTS_REQUIRED_2 | CLOptionDescriptor.DUPLICATES_ALLOWED,
                              DEFINE_OPT,
                              "Define a property used by the generators." ),
      new CLOptionDescriptor( "package",
                              CLOptionDescriptor.ARGUMENT_REQUIRED,
                              PACKAGE_NAME_OPT,
                              "The java package name used to generate artifacts." ),
      new CLOptionDescriptor( "output-directory",
                              CLOptionDescriptor.ARGUMENT_REQUIRED,
                              OUTPUT_DIRECTORY_OPT,
                              "The directory where generated files are output." ),
      new CLOptionDescriptor( "generator",
                              CLOptionDescriptor.ARGUMENT_REQUIRED | CLOptionDescriptor.DUPLICATES_ALLOWED,
                              GENERATOR_OPT,
                              "The name of a generator to run on the model." )
    };

  public static void main( final String[] args )
  {
    System.exit( run( new Environment( Paths.get( "" ).toAbsolutePath(), Logger.getGlobal() ), args ) );
  }

  private static int run( @Nonnull final Environment environment, @Nonnull final String[] args )
  {
    setupLogger( environment );
    if ( !processOptions( environment, args ) )
    {
      return ExitCodes.ERROR_PARSING_ARGS_EXIT_CODE;
    }

    final Logger logger = environment.logger();
    try
    {
      final SchemaRepository schemaRepository = new SchemaRepository();
      final GraphQLSchema schema = schemaRepository.getSchema( environment.getSchemaFiles() );

      final DocumentRepository documentRepository = new DocumentRepository();
      final Document document = documentRepository.getDocument( schema, environment.getDocumentFiles() );

      final Map<String, String> typeMapping =
        MappingUtil.getMapping( environment.getTypeMappingFiles() );
      final Map<String, String> fragmentMapping =
        MappingUtil.getMapping( environment.getFragmentMappingFiles() );

      final List<String> generators = environment.getGenerators();
      if ( !generators.isEmpty() )
      {
        IoUtil.deleteDirIfExists( environment.getOutputDirectory() );
        final GeneratorContext context =
          new GeneratorContext( schema,
                                document,
                                typeMapping,
                                fragmentMapping,
                                environment.getOutputDirectory(),
                                environment.getPackageName() );
        verifyTypeMapping( context );
        for ( final String generator : generators )
        {
          environment.getGeneratorRepository().generate( generator, context );
        }
      }
    }
    catch ( final GenerateException ge )
    {
      logger.log( Level.WARNING,
                  "Error: Failed generating artifacts using generator named " + ge.getName(),
                  ge.getCause() );
      return ExitCodes.UNKNOWN_GENERATOR_EXIT_CODE;
    }
    catch ( final NoSuchGeneratorException nsge )
    {
      logger.log( Level.WARNING, "Error: Unable to locate generator named " + nsge.getName() );
      return ExitCodes.UNKNOWN_GENERATOR_EXIT_CODE;
    }
    catch ( final DocumentReadException dre )
    {
      logger.log( Level.WARNING, dre.getMessage() );
      return ExitCodes.ERROR_READING_DOCUMENT_EXIT_CODE;
    }
    catch ( final DocumentValidateException dve )
    {
      logger.log( Level.WARNING, "Error: Failed to validate document" );
      final List<ValidationError> errors = dve.getErrors();
      for ( final ValidationError error : errors )
      {
        final String locations =
          error.getLocations()
            .stream()
            .map( e -> e.getSourceName() + ":" + e.getLine() )
            .collect( Collectors.joining( " " ) );
        logger.log( Level.WARNING, error.getErrorType() + ":" + error.getMessage() +
                                   ( locations.isEmpty() ? "" : " @ " + locations ) );
      }

      return ExitCodes.ERROR_PARSING_DOCUMENT_EXIT_CODE;
    }
    catch ( final SchemaReadException sre )
    {
      logger.log( Level.WARNING, sre.getMessage() );
      return ExitCodes.ERROR_READING_SCHEMA_EXIT_CODE;
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

      return ExitCodes.ERROR_PARSING_SCHEMA_EXIT_CODE;
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
      return tse.getExitCode();
    }
    catch ( final Throwable t )
    {
      logger.log( Level.WARNING, t.toString(), t );
      return ExitCodes.ERROR_EXIT_CODE;
    }

    return ExitCodes.SUCCESS_EXIT_CODE;
  }

  static void verifyTypeMapping( @Nonnull final GeneratorContext context )
  {
    final GraphQLSchema schema = context.getSchema();
    for ( final Map.Entry<String, String> entry : context.getTypeMapping().entrySet() )
    {
      final String key = entry.getKey();
      final String value = entry.getValue();
      if ( null == schema.getType( key ) )
      {
        throw new TerminalStateException( "Type mapping attempted to map the type named '" + key + "' to " +
                                          value + " but there is no type named '" + key + "'",
                                          ExitCodes.BAD_TYPE_MAPPING_EXIT_CODE );
      }
    }
  }

  private static void setupLogger( @Nonnull final Environment environment )
  {
    final ConsoleHandler handler = new ConsoleHandler();
    handler.setFormatter( new RawFormatter() );
    handler.setLevel( Level.ALL );
    final Logger logger = environment.logger();
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
          if ( fileArgument( environment, option, "schema", environment::addSchemaFile ) )
          {
            return false;
          }
          break;
        }
        case DOCUMENT_FILE_OPT:
        {
          if ( fileArgument( environment, option, "document", environment::addDocumentFile ) )
          {
            return false;
          }
          break;
        }
        case TYPE_MAPPING_FILE_OPT:
        {
          if ( fileArgument( environment, option, "type mapping", environment::addTypeMappingFile ) )
          {
            return false;
          }
          break;
        }
        case FRAGMENT_MAPPING_FILE_OPT:
        {
          if ( fileArgument( environment, option, "fragment mapping", environment::addFragmentMappingFile ) )
          {
            return false;
          }
          break;
        }
        case DEFINE_OPT:
        {
          final String key = option.getArgument( 0 );
          final String value = option.getArgument( 1 );
          if ( environment.getDefines().containsKey( key ) )
          {
            logger.log( Level.SEVERE, "Error: Duplicate property defined specified: " + key );
            return false;
          }
          environment.addDefine( key, value );
          break;
        }
        case OUTPUT_DIRECTORY_OPT:
        {
          final String argument = option.getArgument();
          final Path dir = environment.currentDirectory().resolve( argument ).toAbsolutePath().normalize();
          if ( dir.toFile().exists() && !dir.toFile().isDirectory() )
          {
            final String message =
              "Error: Specified output directory exists and is not a directory. Specified value: " + argument;
            logger.log( Level.SEVERE, message );
            return false;
          }
          environment.setOutputDirectory( dir );
          break;
        }
        case GENERATOR_OPT:
        {
          final String argument = option.getArgument();
          if ( environment.getGenerators().contains( argument ) )
          {
            logger.log( Level.SEVERE, "Error: Duplicate generators specified: " + argument );
            return false;
          }
          environment.addGenerator( argument );
          break;
        }
        case PACKAGE_NAME_OPT:
        {
          environment.setPackageName( option.getArgument() );
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
    if ( !environment.hasOutputDirectory() && !environment.getGenerators().isEmpty() )
    {
      logger.log( Level.SEVERE, "Error: Must specify output directory if a generator is specified." );
      return false;
    }
    if ( !environment.hasPackageName() )
    {
      logger.log( Level.SEVERE, "Error: Must specify output package name." );
      return false;
    }

    printBanner( environment );

    return true;
  }

  static void printBanner( @Nonnull final Environment environment )
  {
    final Logger logger = environment.logger();
    if ( logger.isLoggable( Level.FINE ) )
    {
      logger.log( Level.FINE, "Giggle Starting..." );
      logger.log( Level.FINE, "  Output directory: " + environment.getOutputDirectory() );
      logger.log( Level.FINE, "  Output Package: " + environment.getPackageName() );
      logger.log( Level.FINE, "  Generators: " + environment.getGenerators() );
      logger.log( Level.FINE, "  Schema files: " + environment.getSchemaFiles() );
      logger.log( Level.FINE, "  Document files: " + environment.getDocumentFiles() );
      logger.log( Level.FINE, "  Type mapping files: " + environment.getTypeMappingFiles() );
      logger.log( Level.FINE, "  Fragment mapping files: " + environment.getTypeMappingFiles() );
      final Map<String, String> defines = environment.getDefines();
      if ( !defines.isEmpty() )
      {
        logger.log( Level.FINE, "  Property Defines:" );
        for ( final Map.Entry<String, String> property : defines.entrySet() )
        {
          logger.log( Level.FINE, "    " + property.getKey() + "=" + property.getValue() );
        }
      }
    }
  }

  private static boolean fileArgument( @Nonnull final Environment environment,
                                       @Nonnull final CLOption option,
                                       @Nonnull final String label,
                                       @Nonnull final Consumer<Path> action )
  {
    final String argument = option.getArgument();
    final Path file = environment.currentDirectory().resolve( argument ).toAbsolutePath().normalize();
    if ( !file.toFile().exists() )
    {
      final String message = "Error: Specified graphql " + label + " file does not exist. Specified value: " + argument;
      environment.logger().log( Level.SEVERE, message );
      return true;
    }
    action.accept( file );
    return false;
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
