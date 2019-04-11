package com.sumavision.tetris.cs.program;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ScreenVO extends AbstractBaseVO<ScreenVO, ScreenPO> {

	private Long programId;
	private Long serialNum;
	private Long index;
	private Long mimsId;
	private Long resourceId;

	private String name;
	private String previewUrl;
	private String time;

	@Override
	public ScreenVO set(ScreenPO entity) throws Exception {
		this.setId(entity.getId())
		.setUuid(entity.getUuid())
		.setUpdateTime(entity.getUpdateTime() == null ? "": DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
		.setProgramId(entity.getProgramId())
		.setSerialNum(entity.getSerialNum())
		.setIndex(entity.getScreenIndex())
		.setName(entity.getName())
		.setPreviewUrl(entity.getPreviewUrl())
		.setMimsId(entity.getMimsId())
		.setResourceId(entity.getResourceId());
		return this;
	}

	
	public Long getProgramId() {
		return programId;
	}


	public ScreenVO setProgramId(Long programId) {
		this.programId = programId;
		return this;
	}


	public Long getSerialNum() {
		return serialNum;
	}

	public ScreenVO setSerialNum(Long serialNum) {
		this.serialNum = serialNum;
		return this;
	}

	public Long getIndex() {
		return index;
	}

	public ScreenVO setIndex(Long index) {
		this.index = index;
		return this;
	}

	public Long getMimsId() {
		return mimsId;
	}

	public ScreenVO setMimsId(Long mimsId) {
		this.mimsId = mimsId;
		return this;
	}
	
	public Long getResourceId() {
		return resourceId;
	}

	public ScreenVO setResourceId(Long resourceId) {
		this.resourceId = resourceId;
		return this;
	}


	public String getName() {
		return name;
	}


	public ScreenVO setName(String name) {
		this.name = name;
		return this;
	}


	public String getPreviewUrl() {
		return previewUrl;
	}


	public ScreenVO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}
	
	
}
