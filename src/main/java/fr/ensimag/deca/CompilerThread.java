/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca;

import java.io.File;

/**
 *
 * @author bernajul
 */
public class CompilerThread extends Thread {
  File source;
  CompilerOptions options;
  public CompilerThread(CompilerOptions compilerOptions, File source){
    super();
    this.options=compilerOptions;
    this.source=source;
  }
  
  @Override
  public void run(){
    DecacCompiler compiler = new DecacCompiler(options, source);
    if (compiler.compile()) {
      System.exit(1);
    }  
  }
}
