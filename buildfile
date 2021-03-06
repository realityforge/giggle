require 'buildr/git_auto_version'
require 'buildr/gpg'
require 'buildr/single_intermediate_layout'
require 'buildr/top_level_generate_dir'

Buildr::MavenCentral.define_publish_tasks(:profile_name => 'org.realityforge', :username => 'realityforge')

PACKAGED_DEPS =
  [
    :javax_annotation,
    :getopt4j,
    :javapoet,
    :graphql_java,
    :slf4j_api,
    :slf4j_jdk14,
    :antlr4_runtime
  ]

JSONB_LIBS = [:yasson, :jakarta_json, :jakarta_json_api, :jakarta_json_bind]

desc 'giggle: Generate source code and artifacts from a GraphQL schema and operations'
define 'giggle' do
  project.group = 'org.realityforge.giggle'
  compile.options.source = '1.8'
  compile.options.target = '1.8'
  compile.options.lint = 'all'

  project.version = ENV['PRODUCT_VERSION'] if ENV['PRODUCT_VERSION']

  pom.add_apache_v2_license
  pom.add_github_project('realityforge/giggle')
  pom.add_developer('realityforge', 'Peter Donald')

  desc 'Giggle compiler'
  define 'compiler' do
    pom.dependency_filter = Proc.new {|_| false}
    compile.with PACKAGED_DEPS

    package(:jar)
    package(:sources)
    package(:javadoc)
    package(:jar, :classifier => 'all').tap do |jar|
      jar.with :manifest => { 'Main-Class' => 'org.realityforge.giggle.Main' }
      PACKAGED_DEPS.collect {|dep| Buildr.artifact(dep)}.each do |d|
        jar.merge(d)
      end
    end

    test.using :testng
    test.with :gir, :guiceyloops
    test.options[:properties] = {
      'giggle.fixture_dir' => _('src/test/fixtures'),
      'giggle.fixture.all.libs' => "#{Buildr.artifact(:javax_annotation).to_s}:#{Buildr.artifact(:graphql_java).to_s}",
      'giggle.fixture.java-client.libs' => "#{Buildr.artifact(:javaee_api).to_s}",
      'giggle.fixture.java-cdi-client.libs' => "#{Buildr.artifact(:javaee_api).to_s}:#{Buildr.artifact(:keycloak_jaxrs_client_authfilter).to_s}"
    }
    test.options[:java_args] = %w(-ea)
    test.enhance(Buildr.artifacts(:javaee_api, :keycloak_jaxrs_client_authfilter))
  end

  desc 'Integration Tests'
  define 'integration-tests' do
    test.options[:java_args] = %w(-ea)
    test.options[:properties] = {'user.timezone' => 'Australia/Melbourne'}

    test.using :testng
    test.compile.with :gir,
                      :guiceyloops,
                      JSONB_LIBS,
                      project('compiler').package(:jar),
                      project('compiler').compile.dependencies

    integration_tests(project)
  end

  ipr.add_testng_configuration('compiler',
                               :module => 'compiler',
                               :jvm_args => "-ea -Dgiggle.output_fixture_data=false -Dgiggle.fixture_dir=src/test/fixtures -Dgiggle.fixture.java-cdi-client.libs=#{Buildr.artifact(:javaee_api).to_s}:#{Buildr.artifact(:keycloak_jaxrs_client_authfilter).to_s} -Dgiggle.fixture.java-client.libs=#{Buildr.artifact(:javaee_api).to_s} -Dgiggle.fixture.all.libs=#{Buildr.artifact(:javax_annotation).to_s}:#{Buildr.artifact(:graphql_java).to_s}")
  ipr.add_testng_configuration('integration-tests',
                               :module => 'integration-tests',
                               :jvm_args => '-ea -Dgiggle.output_fixture_data=false')

  iml.excluded_directories << project._('tmp')

  ipr.add_component_from_artifact(:idea_codestyle)
  ipr.add_code_insight_settings
  ipr.add_nullable_manager
  ipr.add_javac_settings('-Xlint:all,-processing,-serial -Werror')
end
