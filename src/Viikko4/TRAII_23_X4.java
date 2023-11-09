package Viikko4;

import fi.uef.cs.tra.DiGraph;
import fi.uef.cs.tra.Vertex;
import java.util.Set;

public interface TRAII_23_X4 {

    /**
     * Palauttaa joukkona kaikki ne suunnatun verkon solmut joihin ei johda yhtÃ¤Ã¤n kaarta.
     * @param G syÃ¶teverkko
     * @return juurisolmujen joukko
     */
    Set<Vertex> juuriSolmut(DiGraph G);

    /**
     * Palauttaa moniko suunnatun verkon G komponentti on puu  (eli
     * sellainen komponentti jossa ei ole paluu-, ristikkÃ¤is- tai etenemiskaaria.
     * @param G syÃ¶teverkko
     * @return ehjien puiden lukumÃ¤Ã¤rÃ¤
     */
    int puidenLukumaara(DiGraph G);
}