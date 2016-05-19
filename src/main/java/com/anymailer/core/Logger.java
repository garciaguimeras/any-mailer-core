package com.anymailer.core;

import org.slf4j.LoggerFactory;

/**
 * Created by noel on 14/12/15.
 */
public class Logger
{

    public static final org.slf4j.Logger console = LoggerFactory.getLogger("ConsoleLogger");

    public static final org.slf4j.Logger traffic = LoggerFactory.getLogger("TrafficLogger");

}
