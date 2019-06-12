package org.realityforge.giggle.generator.java.client;

import graphql.language.Document;
import graphql.language.FragmentDefinition;
import graphql.language.FragmentSpread;
import graphql.language.Selection;
import graphql.language.SelectionSet;
import graphql.language.SelectionSetContainer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

final class FragmentCollector
{
  @Nonnull
  private final Map<String, FragmentDefinition> _fragmentsByName;

  FragmentCollector( @Nonnull final Document document )
  {
    _fragmentsByName = document
      .getDefinitions()
      .stream()
      .filter( definition -> definition instanceof FragmentDefinition )
      .map( definition -> (FragmentDefinition) definition )
      .collect( Collectors.toMap( FragmentDefinition::getName, v -> v, ( a, b ) -> b ) );
  }

  @Nonnull
  List<FragmentDefinition> collectFragments( @Nonnull final SelectionSet selectionSet )
  {
    final Map<String, FragmentDefinition> fragments = new LinkedHashMap<>();
    collectFragments( selectionSet, new ArrayList<>(), fragments );
    return new ArrayList<>( fragments.values() );
  }

  private void collectFragments( @Nullable final SelectionSet selectionSet,
                                 @Nonnull final List<String> visitedFragments,
                                 @Nonnull final Map<String, FragmentDefinition> fragments )
  {
    if ( null != selectionSet )
    {
      for ( final Selection selection : selectionSet.getSelections() )
      {
        if ( selection instanceof SelectionSetContainer )
        {
          collectFragments( ( (SelectionSetContainer) selection ).getSelectionSet(), visitedFragments, fragments );
        }
        else if ( selection instanceof FragmentSpread )
        {
          final String name = ( (FragmentSpread) selection ).getName();
          if ( !visitedFragments.contains( name ) )
          {
            visitedFragments.add( name );
            final FragmentDefinition fragmentDefinition = _fragmentsByName.get( name );
            fragments.put( name, fragmentDefinition );
            collectFragments( fragmentDefinition.getSelectionSet(), visitedFragments, fragments );
          }
        }
      }
    }
  }
}
