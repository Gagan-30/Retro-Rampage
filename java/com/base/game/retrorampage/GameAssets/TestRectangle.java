package com.base.game.retrorampage.GameAssets;

public class TestRectangle
{
    public static void main(String[] args)
    {
        Rect R1 = new Rect(0,0, 9,7);
        Rect R2 = new Rect(7,4, 11,6);
        Rect R3 = new Rect(15,2, 4,6);

        // true, true, false
        System.out.println( R1.overlaps(R2) );
        System.out.println( R2.overlaps(R3) );
        System.out.println( R3.overlaps(R1) );
    }
}
