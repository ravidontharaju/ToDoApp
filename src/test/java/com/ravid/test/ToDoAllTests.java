package com.ravid.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ravid.test.controller.ToDoControllerUTest;
import com.ravid.test.service.ToDoServiceUTest;
import com.ravid.test.service.TwilioServiceUTest;

@RunWith(Suite.class)
@SuiteClasses({ 
	ToDoControllerUTest.class, 
	ToDoServiceUTest.class,
	TwilioServiceUTest.class })
public class ToDoAllTests {

}
