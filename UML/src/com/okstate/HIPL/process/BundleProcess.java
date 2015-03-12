/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.okstate.HIPL.process;

import com.okstate.HIPL.bundle.BundleFile;
import com.okstate.HIPL.util.HIPLJob;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 *
 * @author Sridhar
 */
public class BundleProcess implements HIPLJob{
    
      ArrayList args=new ArrayList<String>();
    

    public BundleProcess(String input,String output, String filename, String inputType, String outputType,Class x){
        generate(input,output,filename,inputType,outputType,x);
    }
    
    public BundleProcess(String input,String output, String filename,Class x){
        generate(input,output,filename,BundleFile.SEQUENCE_FILE,BundleFile.SEQUENCE_FILE,x);
    }
    
    public BundleProcess(String input, String output, Class x){
        generate(input,output,"img.proc",BundleFile.SEQUENCE_FILE,BundleFile.SEQUENCE_FILE,x);
    }
    
    private void generate(String input,String output, String filename, String inputType, String outputType, Class x){
       
        args.add(input);
        output=trim(output);
        output=output+"/"+filename;
        args.add(output);
        args.add(inputType.toLowerCase());
        args.add(outputType.toLowerCase());
        args.add(x.getCanonicalName());
    }
    
    @Override
    public void setInputType(String inputType) {
        args.set(2, inputType.toLowerCase());
    }

    @Override
    public void setOutputType(String outputType) {
        args.set(3,outputType.toLowerCase());
    }

    @Override
    public void init() {
        String[] sri= new String[this.args.size()];
                this.args.toArray(sri);
        if(args.get(2).equals("har") || args.get(3).equals("har")){
            System.out.println("HAR Feature is not available");
            return;
        }
        try {
            ToolRunner.run((Tool) new ImageProcess(), sri);
        } catch (Exception ex) {
            Logger.getLogger(BundleProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setInputPath(String input) {
        args.set(0,input);
    }

    @Override
    public void setOutputPath(String output) {
        args.set(1, output);
    }

    @Override
    public String getInputType() {
        return (String) args.get(2);
    }

    @Override
    public String getOuputType() {
        return (String) args.get(3);
    }

    @Override
    public String getInputPath() {
        return (String) args.get(0);
    }

    @Override
    public String getOutputPath() {
            return (String) args.get(1)+"."+getOuputType();
    }

    public void setClassName(Class x){
        args.set(4,x.getCanonicalName());
    }
    
    public Class getClassName(){
          try {
              return Class.forName((String) args.get(4));
          } catch (ClassNotFoundException ex) {
              Logger.getLogger(BundleProcess.class.getName()).log(Level.SEVERE, null, ex);
          }
          return null;
    }
    private String trim(String output) {
        if(output.charAt(output.length()-1)=='/' || output.charAt(output.length()-1)=='/' ){
            return output.substring(0,output.length()-1);
        }
        return output;
    }
    
}