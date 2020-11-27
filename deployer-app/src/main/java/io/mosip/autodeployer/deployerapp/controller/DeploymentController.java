package io.mosip.autodeployer.deployerapp.controller;

import io.mosip.autodeployer.deployerapp.model.DeploymentModel;
import io.mosip.autodeployer.deployerapp.service.DeploymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/deploy")
public class DeploymentController {
    @Autowired
    private DeploymentService service;

    @PostMapping
    public DeploymentModel redeployUI() {
        return this.service.preregUiRedeployer();
    }
}
