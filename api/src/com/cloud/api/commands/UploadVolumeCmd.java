package com.cloud.api.commands;

import org.apache.log4j.Logger;

import com.cloud.api.ApiConstants;
import com.cloud.api.BaseAsyncCreateCmd;
import com.cloud.api.IdentityMapper;
import com.cloud.api.Implementation;
import com.cloud.api.Parameter;
import com.cloud.api.ServerApiException;
import com.cloud.api.response.VolumeResponse;
import com.cloud.event.EventTypes;
import com.cloud.exception.ConcurrentOperationException;
import com.cloud.exception.InsufficientCapacityException;
import com.cloud.exception.NetworkRuleConflictException;
import com.cloud.exception.ResourceAllocationException;
import com.cloud.exception.ResourceUnavailableException;
import com.cloud.user.UserContext;

@Implementation(description="Uploads a data disk.", responseObject=VolumeResponse.class)
public class UploadVolumeCmd extends BaseAsyncCreateCmd {
	public static final Logger s_logger = Logger.getLogger(UploadVolumeCmd.class.getName());
    private static final String s_name = "uploadvolumeresponse";
	
    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name=ApiConstants.DISPLAY_TEXT, type=CommandType.STRING, required=true, description="the display text of the volume. This is usually used for display purposes.", length=4096)
    private String displayText;

    @Parameter(name=ApiConstants.FORMAT, type=CommandType.STRING, required=true, description="the format for the volume. Possible values include QCOW2, RAW, and VHD.")
    private String format;

    @Parameter(name=ApiConstants.HYPERVISOR, type=CommandType.STRING, required=true, description="the target hypervisor for the volume")
    private String hypervisor;

    @Parameter(name=ApiConstants.NAME, type=CommandType.STRING, required=true, description="the name of the volume")
    private String volumeName;

    @Parameter(name=ApiConstants.URL, type=CommandType.STRING, required=true, description="the URL of where the volume is hosted. Possible URL include http:// and https://")
    private String url;

    @IdentityMapper(entityTableName="data_center")
    @Parameter(name=ApiConstants.ZONE_ID, type=CommandType.LONG, required=true, description="the ID of the zone the volume is to be hosted on")
    private Long zoneId;

    @IdentityMapper(entityTableName="domain")
    @Parameter(name=ApiConstants.DOMAIN_ID, type=CommandType.LONG, description="an optional domainId. If the account parameter is used, domainId must also be used.")
    private Long domainId;

    @Parameter(name=ApiConstants.ACCOUNT, type=CommandType.STRING, description="an optional accountName. Must be used with domainId.")
    private String accountName;

    @Parameter(name=ApiConstants.CHECKSUM, type=CommandType.STRING, description="the MD5 checksum value of this volume")
    private String checksum;

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public String getDisplayText() {
        return displayText;
    }

    public String getFormat() {
        return format;
    }

    public String getHypervisor() {
        return hypervisor;
    }

    public String getVolumeName() {
        return volumeName;
    }
      
    public String getUrl() {
        return url;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public Long getDomainId() {
        return domainId;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getChecksum() {
        return checksum;
    }	
      
    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////
    @Override
	public void create() throws ResourceAllocationException {

	}

	@Override
	public String getEntityTable() {
		return "volumes";
	}

	@Override
	public String getEventDescription() {
		return  "creating volume: " + getVolumeName();
	}

	@Override
	public String getEventType() {
		return EventTypes.EVENT_VOLUME_CREATE;		
	}

	@Override
	public void execute() throws ResourceUnavailableException,
			InsufficientCapacityException, ServerApiException,
			ConcurrentOperationException, ResourceAllocationException,
			NetworkRuleConflictException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getCommandName() {
		   return s_name;
	}

	@Override
	public long getEntityOwnerId() {
		Long accountId = finalyzeAccountId(accountName, domainId, null, true);
        if (accountId == null) {
            return UserContext.current().getCaller().getId();
        }
        
        return accountId;
	}

}
