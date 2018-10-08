package com.ddf.pmay;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Settings extends Fragment {

    TextView name, email, phone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings, container, false);

        name = view.findViewById(R.id.textView10);
        email = view.findViewById(R.id.textView11);
        phone = view.findViewById(R.id.textView12);

        name.setText(SharePreferenceUtils.getInstance().getString("name"));
        email.setText(SharePreferenceUtils.getInstance().getString("email"));
        phone.setText(SharePreferenceUtils.getInstance().getString("phone"));

        return view;
    }
}
