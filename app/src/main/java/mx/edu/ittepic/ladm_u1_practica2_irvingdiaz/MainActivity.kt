package mx.edu.ittepic.ladm_u1_practica2_irvingdiaz

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var entrada = ""
        var nombreArchivo = ""
        //Boton Guardar
        radiogroup.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                Toast.makeText(applicationContext," Has Seleccionado : ${radio.text}",
                    Toast.LENGTH_SHORT).show()

                if(radio.text.toString().equals("Memoria SD")){
                    if(ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        //si entra entonces aún no se otorgaron los permiros
                        //el siguiente código los solicita
                        ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE),0)
                    }else{
                        Toast.makeText(applicationContext,"Permisos a la SD Correctamente",
                            Toast.LENGTH_SHORT).show()
                    }
                }

            })

    button.setOnClickListener {
        entrada = editText2.text.toString()
        nombreArchivo = editText3.text.toString()


        var id : Int = radiogroup.checkedRadioButtonId
        var memoria : Int = memoriaSeleccionada(id)
        if(memoria == 2) {
            Toast.makeText(applicationContext,"No elegiste ningún tipo de Memoria " +
                    "(Memoria Interna, Memoria Externa) GUARDADO NO LOGRADO",
                Toast.LENGTH_SHORT).show()
        }else if(memoria == 0){//memoria interna
            guardarArchivoInterno(nombreArchivo)
        }
        else if(memoria == 1){//memoria externa

            guardarArchivoExterno(nombreArchivo)
        }
    }

        //Boton Abrir
        button2.setOnClickListener {
            entrada = editText2.text.toString()
            nombreArchivo = editText3.text.toString()

            var id : Int = radiogroup.checkedRadioButtonId
            var memoria : Int = memoriaSeleccionada(id)
            if(memoria == 2) {
                Toast.makeText(applicationContext,"No elegiste ningún tipo de Memoria " +
                                    "(Memoria Interna, Memoria Externa) APERTURA NO LOGRADA",
                Toast.LENGTH_SHORT).show()
                }else if(memoria == 0){//memoria interna

                    leerArchivoInterno(nombreArchivo)

                }else if(memoria == 1){//memoria externa

                    Toast.makeText(applicationContext,"lectura memoria externa",Toast.LENGTH_SHORT).show()
                    leerArchivoExterno(nombreArchivo)
        }
    }

    }

    fun memoriaSeleccionada(id : Int) : Int{
        if (id!=-1){
            if(id == 2131165283){
                return 0 //memoria Interna
            }else{
                return 1 //memoria SD
            }
        }else{
            return 2
        }
    }
    
    //metodos memoria interna
    fun guardarArchivoInterno(nombreArchivo:String){
        try{
            var flujoSalida = OutputStreamWriter(
                openFileOutput(nombreArchivo, Context.MODE_PRIVATE))
            var data = editText2.text.toString()

            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()
            mensaje("EXITO! Se guardó correctamente")
            ponerTexto("")
        }catch (error : IOException){
            mensaje(error.message.toString())
        }
    }//fin guardar Archivo Interno

    private fun leerArchivoInterno(nombreArchivo: String){
        try{
            var flujoEntrada = BufferedReader(
                InputStreamReader(
                    openFileInput(nombreArchivo)
                )
            )
            var data = flujoEntrada.readLine()
            ponerTexto(data)
            flujoEntrada.close()

        }catch (error: IOException){
            mensaje(error.message.toString())
        }
    }//fin guardar Archivo Interno

    fun mensaje(m : String){
        AlertDialog.Builder(this)
            .setTitle("Atención")
            .setMessage(m)
            .setPositiveButton("OK"){d,i->}
            .show()
    }//fin mensaje

    //métodos memoria Externa
    fun guardarArchivoExterno(nombreArchivo: String){
        var sdDisponible = false
        var sdAccesoEscritura = false

        try {
            val ruta_sd = Environment.getExternalStorageDirectory()
            var data = editText2.text.toString()
            val Flujo = File(ruta_sd.absolutePath, nombreArchivo)

            val FlujoSalida = OutputStreamWriter(
                FileOutputStream(Flujo)
            )

            FlujoSalida.write(data)
            FlujoSalida.close()
            mensaje("EXITO! Se guardó correctamente")
            ponerTexto("")
        } catch (error: Exception) {
            mensaje(error.message.toString())
        }

    }

    fun leerArchivoExterno(nombreArchivo: String){
        try {
            val ruta_sd = Environment.getExternalStorageDirectory()

            val Flujo = File(ruta_sd.absolutePath, nombreArchivo)

            val FlujoEntrada = BufferedReader(
                InputStreamReader(
                    FileInputStream(Flujo)
                )
            )

            val data = FlujoEntrada.readLine()
            ponerTexto(data)

            FlujoEntrada.close()

        } catch (error: Exception) {
            mensaje(error.message.toString())
        }

    }

    fun ponerTexto(datos : String){
        editText2.setText(datos)
    }

}

