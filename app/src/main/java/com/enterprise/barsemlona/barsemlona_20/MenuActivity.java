package com.enterprise.barsemlona.barsemlona_20;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {/*
    ArrayList<String> nome = new ArrayList<>();
    ArrayList<String> preco = new ArrayList<> ();
    ArrayList<String> desc = new ArrayList<>();
    ArrayList<String> categoriaId = new ArrayList<>();
    ArrayList<String> promocaoDesconto = new ArrayList<>();

    ArrayList<String> precoDescontado = new ArrayList<>();*/
    ProgressDialog progressDialog ;
    FloatingActionButton fab ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
            progressDialog = new ProgressDialog(MenuActivity.this);

           /* NetworkCall myCall = new NetworkCall();
            myCall.execute("http://deltaws.azurewebsites.net/g1/rest/produto");
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading...");
            progressDialog.show();


*/HomeFragment home = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("idCategoria", "noId");
        home.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, home).addToBackStack(null).commit();





        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent i = new Intent(MenuActivity.this, QrCodeActivity.class);
                    startActivityForResult(i, 1);
            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.peludo) {
            CarrinhoFragment carrinho = new CarrinhoFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, carrinho).addToBackStack(null).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {

            HomeFragment fragment = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putString("idCategoria", "noId");
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack("back").commit();
        } else if (id == R.id.nav_sobre) {
            TelaSobreFragment fragmento = new TelaSobreFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragmento).commit();
        }else if (id == R.id.nav_cat)  {
            CategoriaFragment fragmento = new CategoriaFragment ();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragmento).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onActivityResult(int requestCode, int i, Intent data){
        if(requestCode == 1){
            if(RESULT_OK == i){
                String qrCodeProdutoId = data.getStringExtra("qrCode");
                if (qrCodeProdutoId.substring(0, 1).equals("D") ||qrCodeProdutoId.substring(0, 1).equals("d")) {
                    String nova = qrCodeProdutoId.replace(qrCodeProdutoId.substring(0, 1), "");

                    QrckCall qrc = new QrckCall();
                    qrc.execute("http://deltaws.azurewebsites.net/g1/rest/produto/" + nova);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }else{
                    Alerta al = new Alerta();
                   al.getAlert("Qrcode","Código inválido",MenuActivity.this);
                }
                //System.out.println("http://deltaws.azurewebsites.net/g1/rest/produto/"+qrCodeProdutoId);
            }
        }
    }
    private class QrckCall extends AsyncTask<String, Void, String> {
        String retErro;
        @Override
        protected String doInBackground(String... params) {
            try {
                // Cria o objeto de conexão
                HttpURLConnection urlConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                // Executa a requisição pegando os dados
                InputStream in = urlConnection.getInputStream();
                // Cria um leitor para ler a resposta
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                StringBuilder resultado = new StringBuilder();
                String linha = bufferedReader.readLine();
                // Lê linha a linha a resposta e armazena no StringBuilder
                while (linha != null) {
                    resultado.append(linha);
                    linha = bufferedReader.readLine();
                }
                // Transforma o StringBuilder em String, que contém a resposta final
                // Retorna a string final contendo a resposta retornada
                return resultado.toString();
            } catch (Exception e) {
                if(isOnline()) {
                    retErro = "Produto não encontrado.";
                }else {
                    retErro = "Erro na conexão.";
                }

            }
            // Caso tenha dado algum erro, retorna null
            return retErro;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject json = new JSONObject(result);
                DetalheFragment fragmento = new DetalheFragment();
                Bundle bundle = new Bundle();
                bundle.putString("idProduto", json.getString("idProduto"));
                bundle.putString("nomeProduto", json.getString("nomeProduto"));
                bundle.putString("precoProduto",json.getString("precProduto"));
                bundle.putString("descProduto", json.getString("descProduto"));
                bundle.putString("categoriaProduto", json.getString("idCategoria"));
                bundle.putString("descontoProduto",json.getString("descontoPromocao"));
                progressDialog.hide();
                fragmento.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragmento,"DetalheProduto").commit();

            } catch (Exception e) {
                progressDialog.hide();
                Alerta al = new Alerta();
                al.getAlert("Qrcode",result,MenuActivity.this);

            }
        }
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void showFloatingActionButton() {
        this.fab.show();
    }

    public void hideFloatingActionButton() {
        this.fab.hide();
    }

}
