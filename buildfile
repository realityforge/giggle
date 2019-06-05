require 'buildr/git_auto_version'
require 'buildr/gpg'
require 'buildr/single_intermediate_layout'
require 'buildr/top_level_generate_dir'

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
    test.options[:properties] = { 'giggle.fixture_dir' => _('src/test/fixtures') }
    test.options[:java_args] = %w(-ea)
  end

  desc 'Integration Tests'
  define 'integration-tests' do
    test.options[:java_args] = %w(-ea)

    test.using :testng
    test.compile.with :gir, :guiceyloops,
                      project('compiler').package(:jar),
                      project('compiler').compile.dependencies


    generated_dir = _(:target, :generated, :giggle, :test, :java)
    iml.test_source_directories << generated_dir
    task(generated_dir).enhance(['generate'])
    test.compile.enhance(['generate'])
    test.compile.from(generated_dir)
    test.resources.from(generated_dir)

    task 'generate' do
      target = generated_dir
      jar = project('compiler').package(:jar, :classifier => 'all')
      jar.invoke
      Dir["#{_('src/test/java/org/realityforge/giggle/integration/scenarios/*/schema.graphqls')}"].each do |file|
        name = File.basename(File.dirname(file))
        Java::Commands.java %W(-jar #{jar} --package org.realityforge.giggle.integration.scenarios.#{name} --schema #{file} --output-directory #{target} --generator java-server)
      end
    end
  end

  ipr.add_default_testng_configuration(:jvm_args => '-ea -Dgiggle.output_fixture_data=false -Dgiggle.fixture_dir=compiler/src/test/fixtures')

  iml.excluded_directories << project._('tmp')

  ipr.add_component_from_artifact(:idea_codestyle)
end
