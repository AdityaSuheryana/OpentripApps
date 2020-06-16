package e.user.kemah.data;

import android.provider.ContactsContract;

public class DataSetFireIkut {
    String namaTrip,deskripsi,imageTrip;

    public String getNamaTrip() {
        return namaTrip;
    }

    public void setNamaTrip(String namaTrip) {
        this.namaTrip = namaTrip;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getImageTrip() {
        return imageTrip;
    }

    public void setImageTrip(String imageTrip) {
        this.imageTrip = imageTrip;
    }

    public DataSetFireIkut(String namaTrip, String deskripsi, String imageTrip) {
        this.namaTrip = namaTrip;
        this.deskripsi = deskripsi;
        this.imageTrip = imageTrip;
    }
    public DataSetFireIkut(){

    }
}
