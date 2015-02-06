/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okstate.HIPL.util;

/**
 *
 * @author Sridhar
 */
import com.okstate.HIPL.container.HARIndexContainer;
import java.util.Comparator;

public class HARIndexContainerSorter implements Comparator<HARIndexContainer>{

	public int compare(HARIndexContainer arg0, HARIndexContainer arg1) {
		return arg0.hash - arg1.hash;
	}

}