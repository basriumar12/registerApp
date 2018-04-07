package info.blogbasbas.pendaftranonline;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.infideap.atomic.Atom;
import com.infideap.atomic.FutureCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.blogbasbas.pendaftranonline.respon.ResponseInsert;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.edtName)
    EditText edtName;
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.rbLaki)
    RadioButton rbLaki;
    @BindView(R.id.rbWanita)
    RadioButton rbWanita;
    @BindView(R.id.rgKelamin)
    RadioGroup rgKelamin;
    @BindView(R.id.edtAlamat)
    EditText edtAlamat;
    @BindView(R.id.btnRegister)
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnRegister)
    public void onViewClicked() {
        String name = edtName.getText().toString();
        String email = edtEmail.getText().toString();
        String kelamin = "";
        String alamat = edtAlamat.getText().toString();
        int selectedId = rgKelamin.getCheckedRadioButtonId();
        if (selectedId == rbLaki.getId()){
            kelamin = rbLaki.getText().toString();
        } else if (selectedId == rbWanita.getId()){
            kelamin = rbWanita.getText().toString();
        } else {
            Toast.makeText(this, "Gender Belum Centang", Toast.LENGTH_SHORT).show();
        }
        if (name.isEmpty()){
            edtName.setError("name harus di isi");
        } else if (email.isEmpty()){
            edtEmail.setError("email haru di isi");
        } else if (alamat.isEmpty()){
            edtAlamat.setError("alamat harus di isi");
        } else {


            Atom.with(MainActivity.this)
                    .load("https://script.google.com/macros/s/AKfycbwdtTA859qTlClPc4kwRtmg0UrWcreftw_UtHLkJvm0ljpipaQS/exec")
                    .setMultipart("nama", name)
                    .setMultipart("email", email)
                    .setMultipart("jenis_kelamin", kelamin)
                    .setMultipart("alamat", alamat)
                    .as(ResponseInsert.class)
                    .setCallback(new FutureCallback<ResponseInsert>() {
                        @Override
                        public void onCompleted(Exception e, ResponseInsert result) {
                            if (e != null) {
                                e.printStackTrace();
                                return;
                            }

                            Log.d("Tag", "Status" + result.getStatus());
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("Sukses Register");
                            builder.setTitle("Informasi Register");
                            builder.setCancelable(true);
                            builder.setIcon(R.drawable.ic_done_black_24dp);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                    startActivity(getIntent());

                                }
                            });
                            builder.show();
                        }
                    });

        }
    }
}
