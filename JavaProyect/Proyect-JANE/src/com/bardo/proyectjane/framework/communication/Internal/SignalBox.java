package com.bardo.proyectjane.framework.communication.Internal;

import java.sql.Date;

public class SignalBox {

	private Date time;
	private String data;
	private int priority;
	private int module;

	public SignalBox(Date time, int module, String data, int priority) {
		this.time = time;
		this.data = data;
		this.module = module;
		this.priority = priority;
	}

	public int getModule() {
		return this.module;
	}

	public Date getTime() {
		return this.time;
	}

	public String getData() {
		return this.data;
	}

	public int getPriority() {
		return this.priority;
	}

}
