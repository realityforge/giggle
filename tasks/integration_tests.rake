def integration_tests(project)
  desc 'Generate code required for integration tests'
  generate_task = project.task('generate')
  jar = project('compiler').package(:jar, :classifier => 'all')
  # This next line forces the creation of config thus avoiding the
  # iml also including the generated as a resource directory
  project.iml.test_resource_directories
  Dir["#{_('src/test/java/org/realityforge/giggle/integration/scenarios/*/schema.graphqls')}"].each do |file|
    name = File.basename(File.dirname(file))
    generated_dir = project._(:target, :generated, name, :test, :java)
    project.iml.test_source_directories << generated_dir
    t = project.task(generated_dir) do
      jar.invoke
      Java::Commands.java %W(-jar #{jar} --package org.realityforge.giggle.integration.scenarios.#{name} --schema #{file} --output-directory #{generated_dir} --generator java-server)
    end
    generate_task.enhance([t.name])
    project.test.compile.enhance([t.name])
    project.test.compile.from(generated_dir)
    project.test.resources.from(generated_dir)
  end
end
