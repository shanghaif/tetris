package com.sumavision.tetris.record.task;

import java.util.Date;

import javax.persistence.Column;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 
 * 录制任务 子任务PO 一个recordtaskItem 可以看做一次具体收录动作从启动 到 停止 的过程
 * 一个录制任务对应N个录制子任务，比如多个定时任务，每一个定时周期是一个收录子任务
 * 
 * 
 * @author chenmo
 * 
 * 
 * 
 */
public class RecordTaskItemPO extends AbstractBasePO {

	private static final long serialVersionUID = 1L;

	// 子任务name，节目名称之类
	private String name;

	// 录制任务（父任务）Id
	private Long recordTaskId;

	// 子任务具体开始时间
	private Date startTime;

	// 子任务具体的结束时间
	private Date stopTime;

	// 录制状态,0,未录制，1：正在录制，2，录制完成，3：录制失败，4：暂停
	@Column(name = "status")
	private Integer status;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getRecordTaskId() {
		return recordTaskId;
	}

	public void setRecordTaskId(Long recordTaskId) {
		this.recordTaskId = recordTaskId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStopTime() {
		return stopTime;
	}

	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}

}
