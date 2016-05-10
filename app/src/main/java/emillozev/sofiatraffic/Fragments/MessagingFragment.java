package emillozev.sofiatraffic.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import emillozev.sofiatraffic.R;
import emillozev.sofiatraffic.UI.MainActivity;

public class MessagingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflator = inflater.inflate(R.layout.message_fragment, container, false);
        Button startMessaging = (Button) inflator.findViewById(R.id.startMessaging);

        startMessaging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                Intent intent = new Intent(mainActivity, emillozev.sofiatraffic.Messaging.Message.class);
                startActivity(intent);
            }
        });

        return inflator;
    }

}
