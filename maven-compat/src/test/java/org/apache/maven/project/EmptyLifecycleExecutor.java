package org.apache.maven.project;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.lifecycle.*;
import org.apache.maven.lifecycle.LifecycleExecutionException;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.plugin.InvalidPluginDescriptorException;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoNotFoundException;
import org.apache.maven.plugin.PluginDescriptorParsingException;
import org.apache.maven.plugin.PluginNotFoundException;
import org.apache.maven.plugin.PluginResolutionException;
import org.apache.maven.plugin.prefix.NoPluginFoundForPrefixException;
import org.apache.maven.plugin.version.PluginVersionResolutionException;

/**
 * A stub implementation that assumes an empty lifecycle to bypass interaction with the plugin manager and to avoid
 * plugin artifact resolution from repositories.
 * 
 * @author Benjamin Bentmann
 */
public class EmptyLifecycleExecutor
    implements LifecycleExecutor
{

    public MavenExecutionPlan calculateExecutionPlan( MavenSession session, String... tasks )
        throws PluginNotFoundException, PluginResolutionException, PluginDescriptorParsingException,
        MojoNotFoundException
    {
        return new MavenExecutionPlan(null, null, null, new DefaultLifecycles() );
    }

    public void execute( MavenSession session )
    {
    }

    public Set<Plugin> getPluginsBoundByDefaultToAllLifecycles( String packaging )
    {
        Set<Plugin> plugins;

        // NOTE: The upper-case packaging name is intentional, that's a special hinting mode used for certain tests
        if ( "JAR".equals( packaging ) )
        {
            plugins = new LinkedHashSet<Plugin>();

            plugins.add( newPlugin( "maven-compiler-plugin", "compile", "testCompile" ) );
            plugins.add( newPlugin( "maven-resources-plugin", "resources", "testResources" ) );
            plugins.add( newPlugin( "maven-surefire-plugin", "test" ) );
            plugins.add( newPlugin( "maven-jar-plugin", "jar" ) );
            plugins.add( newPlugin( "maven-install-plugin", "install" ) );
            plugins.add( newPlugin( "maven-deploy-plugin", "deploy" ) );
        }
        else
        {
            plugins = Collections.emptySet();
        }

        return plugins;
    }

    private Plugin newPlugin( String artifactId, String... goals )
    {
        Plugin plugin = new Plugin();

        plugin.setGroupId( "org.apache.maven.plugins" );
        plugin.setArtifactId( artifactId );

        for ( String goal : goals )
        {
            PluginExecution pluginExecution = new PluginExecution();
            pluginExecution.setId( "default-" + goal );
            pluginExecution.addGoal( goal );
            plugin.addExecution( pluginExecution );
        }

        return plugin;
    }

    public void calculateForkedExecutions( MojoExecution mojoExecution, MavenSession session )
        throws MojoNotFoundException, PluginNotFoundException, PluginResolutionException,
        PluginDescriptorParsingException, NoPluginFoundForPrefixException, InvalidPluginDescriptorException,
        LifecyclePhaseNotFoundException, LifecycleNotFoundException, PluginVersionResolutionException
    {
    }

    public List<MavenProject> executeForkedExecutions( MojoExecution mojoExecution, MavenSession session )
        throws LifecycleExecutionException
    {
        return Collections.emptyList();
    }

}
