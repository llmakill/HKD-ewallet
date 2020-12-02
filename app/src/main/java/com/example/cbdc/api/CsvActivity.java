package com.example.cbdc.api;

import com.example.cbdc.dao.Userinfo;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class CsvActivity
{
    public ArrayList<Userinfo> readCsv(File path)
    {
        ArrayList<Userinfo> readerArr = new ArrayList<>();
        File file = new File(path, "Userinfo.csv");
        System.out.println(file.getAbsolutePath());
        FileInputStream fileInputStream;
        Scanner in;
        try
        {
            fileInputStream = new FileInputStream(file);
            in = new Scanner(fileInputStream, "UTF-8");
            in.nextLine();
            while (in.hasNextLine()) {
                String[] lines = in.nextLine().split(",");
                Userinfo user_info = new Userinfo();
                user_info.setName(lines[0]);
                user_info.setIdcard(lines[1]);
                user_info.setPhonenum(lines[2]);
                user_info.setPassword(lines[3]);
                user_info.setPaypassword(lines[4]);
                readerArr.add(user_info);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return readerArr;
    }
}
