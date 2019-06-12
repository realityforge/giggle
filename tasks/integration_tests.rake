def integration_tests(project)
  desc 'Generate code required for integration tests'
  generate_task = project.task('generate')
  jar = project('compiler').package(:jar, :classifier => 'all')
  # This next line forces the creation of config thus avoiding the
  # iml also including the generated as a resource directory
  project.iml.test_resource_directories
  Dir["#{_('src/test/java/org/realityforge/giggle/integration/scenarios/*/schema.graphqls')}"].each do |file|
    name = File.basename(File.dirname(file))
    document_file = "#{File.dirname(file)}/document.graphql"
    gentasks = []
    gentasks << %W(#{name}-server java-server server)
    gentasks << %W(#{name}-client java-client client) if File.exist?(document_file)
    gentasks.each do |dirname, generator, package_suffix|
      generated_dir = project._(:target, :generated, dirname, :test, :java)
      project.iml.test_source_directories << generated_dir
      t = project.task(generated_dir) do
        jar.invoke
        args = []
        args << '-jar'
        args << jar.to_s
        args << '--package'
        args << "org.realityforge.giggle.integration.scenarios.#{name}.#{package_suffix}"
        args << '--schema'
        args << file.to_s
        if File.exist?(document_file)
          args << '--document'
          args << document_file.to_s
        end
        args << '--output-directory'
        args << generated_dir.to_s
        args << '--generator'
        args << generator
        Java::Commands.java args
      end
      generate_task.enhance([t.name])
      project.test.compile.enhance([t.name])
      project.test.compile.from(generated_dir)
      project.test.resources.from(generated_dir)
    end
  end
  project.test.resources.from(project._('src/test/java'))
end
