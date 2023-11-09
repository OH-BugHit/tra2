package Viikko4;

import fi.uef.cs.tra.DiGraph;
import fi.uef.cs.tra.Vertex;

import java.util.HashSet;
import java.util.Set;


public class TRAII_23_X4_hilkolli implements TRAII_23_X4 {

    /**
     * ITSEARVIOINTI TÃ„HÃ„N:
     *
     *
     *
     **/
    
    /** Palauttaa joukkona kaikki ne suunnatun verkon solmut joihin ei johda yhtÃ¤Ã¤n kaarta.
     *
     * @param G syÃ¶teverkko
     * @return juurisolmujen joukko
     */
    @Override
    public Set<Vertex> juuriSolmut(DiGraph G) {
        Set<Vertex> tulos = new HashSet<>();

        // TODO, apumetodeja saa kÃ¤yttÃ¤Ã¤

        return tulos;
    }

    /**
     * Palauttaa moniko suunnatun verkon G komponentti on puu  (eli
     * sellainen komponentti jossa ei ole paluu-, ristikkÃ¤is- tai etenemiskaaria.
     *
     * @param G syÃ¶teverkko
     * @return ehjien puiden lukumÃ¤Ã¤rÃ¤
     */
    @Override
    public int puidenLukumaara(DiGraph G) {

        Set<Vertex> potentiaalisetPuunJuuret = juuriSolmut(G);
        // juurisolmujen hakua ei ole pakko kÃ¤yttÃ¤Ã¤, mutta se on hyvÃ¤ lÃ¤htÃ¶kohta
        // juurisolmujen haku on kuitenkin pakollinen osa tehtÃ¤vÃ¤Ã¤

        // TODO tÃ¤hÃ¤n sitten koodia jossa selvitetÃ¤Ã¤n mitkÃ¤ komponentit
        // TODO oikeasti ovat puita, apumetodeja saa kÃ¤yttÃ¤Ã¤

        // TODO apumetodeja saa kÃ¤yttÃ¤Ã¤

        return 0; // TODO: tÃ¤hÃ¤n oikea palautusarvo
    }
}