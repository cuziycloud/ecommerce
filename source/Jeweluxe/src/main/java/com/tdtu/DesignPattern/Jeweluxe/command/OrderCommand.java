package com.tdtu.DesignPattern.Jeweluxe.command;

// command Interface: Khai báo phương thức thực thi chung.
public interface OrderCommand {

    Object execute() throws Exception; 

    // Optional: void undo();
}