package com.example.cbdc.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbdc.R;
import com.example.cbdc.dao.Userinfo;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class RegActivity extends Activity
{
    public Userinfo user_info = new Userinfo();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registor);

        TextView t_login_text = (TextView) findViewById(R.id.login_text);
        final String login_text = t_login_text.getText().toString();

        TextView t_username = (TextView) findViewById(R.id.username);
        final String username = t_username.getText().toString();

        final EditText shuruxingming = (EditText) findViewById(R.id.shuruxingming);

        TextView t_idcard = (TextView) findViewById(R.id.idcard);
        final String idcard = t_idcard.getText().toString();

        final EditText shurushenfenzheng = (EditText) findViewById(R.id.shurushenfenzheng);

        TextView t_phonenum = (TextView) findViewById(R.id.phonenum);
        final String phonenum = t_phonenum.getText().toString();

        final EditText shuruphonenum = (EditText) findViewById(R.id.shuruphonenum);

        // 获取验证码
        final Button btn_getcode = findViewById(R.id.btn_getcode);

        TextView t_veri_code = (TextView) findViewById(R.id.veri_code);
        final String veri_code = t_veri_code.getText().toString();

        final EditText shuruyanzhengma = (EditText) findViewById(R.id.shuruyanzhengma);

        // 验证验证码
        final Button btn_checkcode = findViewById(R.id.btn_checkcode);

        TextView t_password = (TextView) findViewById(R.id.password);
        final String password = t_password.getText().toString();

        final EditText shurupassword = (EditText) findViewById(R.id.shurupassword);

        TextView t_re_password = (TextView) findViewById(R.id.re_password);
        final String re_password = t_re_password.getText().toString();

        final EditText shururepassword = (EditText) findViewById(R.id.shururepassword);

        TextView t_pay_password = (TextView) findViewById(R.id.pay_password);
        final String pay_password = t_password.getText().toString();

        final EditText shurupaypassword = (EditText) findViewById(R.id.shurupaypassword);

        TextView t_querenpay_password = (TextView) findViewById(R.id.querenpay_password);
        final String querenpay_password = t_password.getText().toString();

        final EditText shururepaypassword = (EditText) findViewById(R.id.shururepaypassword);

        final Button btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                user_info.setName(shuruxingming.getText().toString());
                user_info.setIdcard(shurushenfenzheng.getText().toString());
                user_info.setPhonenum(shuruphonenum.getText().toString());
                String input_password = shurupassword.getText().toString();
                String check_password = shururepassword.getText().toString();
                String input_paypassword = shurupaypassword.getText().toString();
                String check_paypassword = shururepaypassword.getText().toString();
                if (input_password.equals(check_password) && input_paypassword.equals(check_paypassword))
                {
                    user_info.setPassword(input_password);
                    user_info.setPaypassword(input_paypassword);
                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
                    File extDir = cw.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                    File writeFile = new File(extDir, "Userinfo.csv");
                    System.out.println(writeFile.getAbsolutePath());
                    try
                    {
                        BufferedWriter writeText = new BufferedWriter(new FileWriter(writeFile, true));
                        //调用write的方法将字符串写到流中
                        writeText.write(user_info.toString());
                        writeText.newLine();
                        //使用缓冲区的刷新方法将数据刷到目的地中
                        writeText.flush();
                        //关闭缓冲区，缓冲区没有调用系统底层资源，真正调用底层资源的是FileWriter对象，缓冲区仅仅是一个提高效率的作用
                        //因此，此处的close()方法关闭的是被缓存的流对象
                        writeText.close();
                    } catch (FileNotFoundException e) {
                        System.out.println("没有找到指定文件");
                        System.out.println(e);
                    } catch (IOException e) {
                        System.out.println("文件读写出错");
                    }
                    Intent intent = new Intent(RegActivity.this, LoginActivity.class);
                    Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                else if (!input_password.equals(check_password)) {
                    Toast.makeText(getApplicationContext(), "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                }
                else if (!input_paypassword.equals(check_paypassword)) {
                    Toast.makeText(getApplicationContext(), "两次输入的支付密码不一致", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "您输入的信息有误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final ImageButton huiqu = findViewById(R.id.huiqu);
        huiqu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
