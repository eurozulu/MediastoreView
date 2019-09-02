package org.spoofer.mediastoreview.mediagroup;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class SimpleQuery implements MediaGroup.Query, Parcelable {

    private final String name;
    private final Uri query;

    public SimpleQuery(Parcel in) {
        String[] inArray = new String[2];
        in.readStringArray(inArray);
        this.name = inArray[0];
        this.query = Uri.parse(inArray[1]);
    }

    public SimpleQuery(String name, Uri query) {
        this.name = name;
        this.query = query;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Uri getQuery() {
        return query;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {
                this.name,
                this.query.toString()
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MediaGroup.Query createFromParcel(Parcel in) {
            return new SimpleQuery(in);
        }

        public MediaGroup.Query[] newArray(int size) {
            return new MediaGroup.Query[size];
        }
    };

    @Override
    public String toString() {
        return String.format("%s[%s - %s]", super.toString(), name, query.toString());
    }
}
