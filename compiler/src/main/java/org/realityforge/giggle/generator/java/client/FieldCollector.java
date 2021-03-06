package org.realityforge.giggle.generator.java.client;

import graphql.execution.MergedField;
import graphql.execution.MergedSelectionSet;
import graphql.language.Document;
import graphql.language.Field;
import graphql.language.FragmentDefinition;
import graphql.language.FragmentSpread;
import graphql.language.InlineFragment;
import graphql.language.Selection;
import graphql.language.SelectionSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import static graphql.execution.MergedSelectionSet.*;

final class FieldCollector
{
  @Nonnull
  private final Map<String, FragmentDefinition> _fragmentsByName;

  FieldCollector( @Nonnull final Document document )
  {
    _fragmentsByName = document
      .getDefinitions()
      .stream()
      .filter( definition -> definition instanceof FragmentDefinition )
      .map( definition -> (FragmentDefinition) definition )
      .collect( Collectors.toMap( FragmentDefinition::getName, v -> v, ( a, b ) -> b ) );
  }

  MergedSelectionSet collectFields( @Nonnull final SelectionSet... selectionSets )
  {
    final Map<String, MergedField> subFields = new LinkedHashMap<>();
    for ( final SelectionSet selectionSet : selectionSets )
    {
      collectFields( selectionSet, new ArrayList<>(), subFields );
    }
    return newMergedSelectionSet().subFields( subFields ).build();
  }

  private void collectFields( @Nonnull final SelectionSet selectionSet,
                              @Nonnull final List<String> visitedFragments,
                              @Nonnull final Map<String, MergedField> fields )
  {

    for ( final Selection<?> selection : selectionSet.getSelections() )
    {
      if ( selection instanceof Field )
      {
        collectField( fields, (Field) selection );
      }
      else if ( selection instanceof InlineFragment )
      {
        collectFields( ( (InlineFragment) selection ).getSelectionSet(), visitedFragments, fields );
      }
      else
      {
        assert selection instanceof FragmentSpread;
        final String name = ( (FragmentSpread) selection ).getName();
        if ( !visitedFragments.contains( name ) )
        {
          visitedFragments.add( name );
          collectFields( _fragmentsByName.get( name ).getSelectionSet(), visitedFragments, fields );
        }
      }
    }
  }

  private void collectField( @Nonnull final Map<String, MergedField> fields, @Nonnull final Field field )
  {
    final String alias = field.getAlias();
    final String name = null != alias ? alias : field.getName();
    final MergedField existing = fields.get( name );
    if ( null != existing )
    {
      fields.put( name, existing.transform( builder -> builder.addField( field ) ) );
    }
    else
    {
      fields.put( name, MergedField.newMergedField( field ).build() );
    }
  }
}
