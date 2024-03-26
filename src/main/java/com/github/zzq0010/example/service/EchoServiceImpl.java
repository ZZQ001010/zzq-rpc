/**
 * 
 */
package com.github.zzq0010.example.service;

/**
 * @author zzq
 *
 */
public class EchoServiceImpl implements EchoService {

    @Override
    public String echo(String echo) {
        return echo;
    }

    @Override
    public String hello(String name) {
        return "Hello " + name;
    }

}
