package io.mosip.autodeployer.deployerapp.controller;

import io.mosip.autodeployer.deployerapp.model.DeploymentModel;
import io.mosip.autodeployer.deployerapp.service.DeploymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/deploy")
public class DeploymentController {
    @Autowired
    private DeploymentService service;

    @PostMapping("/{app}")
    public DeploymentModel redeployUI(
            @PathVariable String app
    ) {
        return this.service.preregUiRedeployer(app);
    }
}
