/**
 *  Copyright (c) 2013-2016 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package tern.eclipse.ide.server.nodejs.core.debugger.launchConfigurations;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.variables.VariablesPlugin;

import tern.eclipse.ide.server.nodejs.core.debugger.INodejsDebugger;
import tern.eclipse.ide.server.nodejs.core.debugger.NodejsDebuggersManager;
import tern.utils.StringUtils;

/**
 * Variable helper.
 *
 */
public class NodejsCliFileHelper {

	private static final String WORKSPACE_LOC = "workspace_loc";

	public static IFile getConfigFile(String param) throws NodejsCliFileConfigException, CoreException {
		if (StringUtils.isEmpty(param)) {
			throw new NodejsCliFileConfigException("Configuration file cannot be empty.");
		}
		IFile configFile = getResource(param);
		if (configFile != null && configFile.exists()) {
			return configFile;
		}
		throw new NodejsCliFileConfigException("Cannot find configuration file");
	}

	public static IFile getCliFile(String param) throws NodejsCliFileConfigException, CoreException {
		if (StringUtils.isEmpty(param)) {
			throw new NodejsCliFileConfigException("Client file cannot be empty.");
		}
		IFile configFile = getResource(param);
		if (configFile != null && configFile.exists()) {
			return configFile;
		}
		throw new NodejsCliFileConfigException("Cannot find client file");
	}

	public static File getNodeInstallPath(String param) throws NodejsCliFileConfigException, CoreException {
		if (StringUtils.isEmpty(param)) {
			throw new NodejsCliFileConfigException("Node.js install path cannot be empty.");
		}
		File nodeInstallPath = new File(param);
		if (nodeInstallPath.exists()) {
			return nodeInstallPath;
		}
		throw new NodejsCliFileConfigException("Cannot find node install path " + nodeInstallPath.toString());
	}

	public static INodejsDebugger getDebugger(String debuggerId) throws CoreException, NodejsCliFileConfigException {
		if (StringUtils.isEmpty(debuggerId)) {
			throw new NodejsCliFileConfigException("Tern debugger cannot be empty.");
		}
		INodejsDebugger debugger = NodejsDebuggersManager.getDebugger(debuggerId);
		if (debugger != null) {
			return debugger;
		}
		throw new NodejsCliFileConfigException("Cannot find tern debugger with id" + debuggerId);
	}

	public static String getWorkspaceLoc(IResource file) {
		return VariablesPlugin.getDefault().getStringVariableManager().generateVariableExpression(WORKSPACE_LOC,
				file.getFullPath().toString());
	}

	public static IFile getResource(String path) throws CoreException {
		String location = VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(path);
		return getFileForLocation(location);
	}

	public static IFile getFileForLocation(String path) {
		if (path == null) {
			return null;
		}
		IPath filePath = new Path(path);
		IFile file = null;
		IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(filePath);
		if (files.length > 0) {
			return files[0];
		}
		return null;
	}
}
