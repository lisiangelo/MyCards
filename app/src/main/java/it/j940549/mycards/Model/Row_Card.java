package it.j940549.mycards.Model;

import java.io.Serializable;

/**
 * Created by J940549 on 23/04/2017.
 */

public class Row_Card implements Serializable {
    String nr_barcode;
    String nomeCard;
    String patch_Image_Front="";
    String patch_Image_Retro="";

    public Row_Card(){

    }

  public String getNr_barcode() {
    return nr_barcode;
  }

  public void setNr_barcode(String nr_barcode) {
    this.nr_barcode = nr_barcode;
  }

  public String getNomeCard() {
    return nomeCard;
  }

  public void setNomeCard(String nomeCard) {
    this.nomeCard = nomeCard;
  }

  public String getPatch_Image_Front() {
    return patch_Image_Front;
  }

  public void setPatch_Image_Front(String patch_Image_Front) {
    this.patch_Image_Front = patch_Image_Front;
  }

  public String getPatch_Image_Retro() {
    return patch_Image_Retro;
  }

  public void setPatch_Image_Retro(String patch_Image_Retro) {
    this.patch_Image_Retro = patch_Image_Retro;
  }


}
