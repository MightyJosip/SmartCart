package com.example.smartcart;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartcart.database.Popis;
import com.example.smartcart.database.Stavka;

import org.junit.Test;
import org.mockito.Mockito;


import java.sql.Time;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTests{

    @Test
    public void stavkaTest(){

        Stavka stavka = new Stavka(1234, 4321, "123456789", 17.69, "2",
                true, 5, "mlijeko", false, 54321);

        assertEquals("mlijeko  1234  54321  123456789", stavka.toString());
    }

    @Test
    public void popisToStringTest(){

        Popis popis = new Popis(1234, "moj popis", 1, 54321);

        assertEquals("1234 moj popis 1 54321",popis.toString());
    }

    @Test
    public void trgovinaTest() {

        Trgovina trg = new Trgovina(1, "trg1", "Albaharijeva 21", Time.valueOf("08:00:00"), Time.valueOf("20:00:00"), 42);

        assertEquals("Albaharijeva 21", trg.getAdresa_trgovina());
    }

    @Test
    public void proizvodTest() {

        Proizvod proizvod = new Proizvod("maslac", "200dag", 123456789,
                "Hrvatska", 225, 5, null);

        assertEquals(Integer.valueOf(123456789), proizvod.getBarkod());
    }
}