/*
 * Copyright 2016-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.facebook.buck.intellij.plugin.actions;

import com.facebook.buck.intellij.plugin.build.BuckBuildCommandHandler;
import com.facebook.buck.intellij.plugin.build.BuckBuildManager;
import com.facebook.buck.intellij.plugin.build.BuckCommand;
import com.facebook.buck.intellij.plugin.config.BuckModule;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import icons.BuckIcons;

/**
 * Run buck project command.
 */
public class BuckProjectGenerationAction extends BuckBaseAction {

  public static final String ACTION_TITLE = "Run buck project";
  public static final String ACTION_DESCRIPTION = "Run buck project command";

  public BuckProjectGenerationAction() {
    super(ACTION_TITLE, ACTION_DESCRIPTION, BuckIcons.ACTION_PROJECT);
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    Project project = e.getProject();
    BuckBuildManager buildManager = BuckBuildManager.getInstance(project);

    String target = buildManager.getCurrentSavedTarget(project);
    BuckModule buckModule = project.getComponent(BuckModule.class);
    buckModule.attach(target);

    if (target == null) {
      buildManager.showNoTargetMessage(project);
      return;
    }

    // Initiate a buck build
    BuckBuildCommandHandler handler = new BuckBuildCommandHandler(
        project,
        project.getBaseDir(),
        BuckCommand.PROJECT);
    handler.command().addParameter(target);
    buildManager.runBuckCommandWhileConnectedToBuck(handler, ACTION_TITLE, buckModule);
  }
}
