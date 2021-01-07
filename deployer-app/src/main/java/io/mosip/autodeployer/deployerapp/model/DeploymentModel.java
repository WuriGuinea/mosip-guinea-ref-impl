package io.mosip.autodeployer.deployerapp.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeploymentModel {
   private int status;
   private String desc;
}
