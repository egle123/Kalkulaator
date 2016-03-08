package com.example.egle.kalkulaator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String current = "";
    private String previous = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clear:
                current = "";
                updateText();
                break;
            case R.id.number1:
                numbers(1);
                break;
            case R.id.number2:
                numbers(2);
                break;
            case R.id.number3:
                numbers(3);
                break;
            case R.id.number4:
                numbers(4);
                break;
            case R.id.number5:
                numbers(5);
                break;
            case R.id.number6:
                numbers(6);
                break;
            case R.id.number7:
                numbers(7);
                break;
            case R.id.number8:
                numbers(8);
                break;
            case R.id.number9:
                numbers(9);
                break;
            case R.id.number0:
                numbers(0);
                break;
            case R.id.plus:
                operation("+");
                break;
            case R.id.minus:
                operation("-");
                break;
            case R.id.multiplier:
                operation("*");
                break;
            case R.id.divider:
                operation("/");
                break;
            case R.id.equals:
                if(current.length() > 0) {
                    char last = current.charAt(current.length()-1);
                    if(last < '0' || last > '9'){
                        current = current.substring(0, current.length()-1);
                    }
                    double result = eval(current);
                    previous = current + "=" + result;
                    current = "";
                    updateText();
                }
                break;
            case R.id.backspace:
                if(current.length()>0){
                    current = current.substring(0, current.length()-1);
                    updateText();
                }
                break;
            case R.id.period:
                char last = current.charAt(current.length()-1);
                if(last == '.'){}
                else if (last >= '0' && last <= '9') {
                    for (int i = current.length() - 1; i >= 0; i--) {
                        char ch = current.charAt(i);
                        if (ch < '0' || ch > '9') {
                            if (ch != '.') {
                                current = current + ".";
                                break;
                            } else break;
                        }
                        if (i == 0) {
                            current = current + ".";
                        }
                    }
                }else{
                    current = current + "0.";
                }
                updateText();
                break;
            case R.id.sign:
                break;
        }
    }

    public void numbers(int nr){
        current = current + nr;
        updateText();
    }

    public void operation(String op){
        if (current.length() > 0){
            char last = current.charAt(current.length()-1);
            if (last == '+' || last == '-' || last == '*' || last == '/' || last == '.'){
                current = current.substring(0, current.length()-1)+op;
            }else current = current+op;
            updateText();
        }
    }

    private void updateText(){
        ((TextView)findViewById(R.id.textView)).setText(current + "\n" + previous);
    }

    //selle meetodi võtsin internetist :) http://stackoverflow.com/questions/3422673/evaluating-a-math-expression-given-in-string-form
    public static double eval(final String str) {
        class Parser {
            int pos = -1, c;

            void eatChar() {
                c = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            void eatSpace() {
                while (Character.isWhitespace(c)) eatChar();
            }

            double parse() {
                eatChar();
                double v = parseExpression();
                if (c != -1) throw new RuntimeException("Unexpected: " + (char)c);
                return v;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor | term brackets
            // factor = brackets | number | factor `^` factor
            // brackets = `(` expression `)`

            double parseExpression() {
                double v = parseTerm();
                for (;;) {
                    eatSpace();
                    if (c == '+') { // addition
                        eatChar();
                        v += parseTerm();
                    } else if (c == '-') { // subtraction
                        eatChar();
                        v -= parseTerm();
                    } else {
                        return v;
                    }
                }
            }

            double parseTerm() {
                double v = parseFactor();
                for (;;) {
                    eatSpace();
                    if (c == '/') { // division
                        eatChar();
                        v /= parseFactor();
                    } else if (c == '*' || c == '(') { // multiplication
                        if (c == '*') eatChar();
                        v *= parseFactor();
                    } else {
                        return v;
                    }
                }
            }

            double parseFactor() {
                double v;
                boolean negate = false;
                eatSpace();
                if (c == '+' || c == '-') { // unary plus & minus
                    negate = c == '-';
                    eatChar();
                    eatSpace();
                }
                if (c == '(') { // brackets
                    eatChar();
                    v = parseExpression();
                    if (c == ')') eatChar();
                } else { // numbers
                    int startIndex = this.pos;
                    while ((c >= '0' && c <= '9') || c == '.') eatChar();
                    if (pos == startIndex) throw new RuntimeException("Unexpected: " + (char)c);
                    v = Double.parseDouble(str.substring(startIndex, pos));
                }

                eatSpace();
                if (c == '^') { // exponentiation
                    eatChar();
                    v = Math.pow(v, parseFactor());
                }
                if (negate) v = -v; // unary minus is applied after exponentiation; e.g. -3^2=-9
                return v;
            }
        }
        return new Parser().parse();
    }
}
