//package szymendera_development.guwon_app
//
//import android.content.Intent
//import android.content.SharedPreferences
//import android.os.Bundle
//import android.support.design.widget.NavigationView
//import android.support.v4.view.GravityCompat
//import android.support.v7.app.ActionBarDrawerToggle
//import android.support.v7.app.AppCompatActivity
//import android.view.Menu
//import android.view.MenuItem
//import android.view.View
//import android.widget.ImageView
//import kotlinx.android.synthetic.main.activity_profil_pruefung.*
//import kotlinx.android.synthetic.main.app_bar_profil_pruefung.*
//import szymendera_development.guwon_app.Utils.*
//
//class ProfilPruefungActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
//
//    var sp: SharedPreferences? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_profil_pruefung)
//        setSupportActionBar(toolbar)
//
//        val toggle = ActionBarDrawerToggle(
//                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
//        drawer_layout.addDrawerListener(toggle)
//        toggle.syncState()
//
//        nav_view.setNavigationItemSelectedListener(this)
//
//        sp = getSharedPreferences("com.szymendera_development.guwon_app", MODE_PRIVATE)
//
//        // Making the Logo in the NavBar-Header clickable
//        val navigationView = findViewById<NavigationView>(R.id.nav_view) as NavigationView
//        navigationView.setNavigationItemSelectedListener(this)
//        val headerview = navigationView.getHeaderView(0)
//        val navheaderlogo = headerview.findViewById<ImageView>(R.id.navHeaderLogo)
//        navheaderlogo.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(v: View) {
//                val intent = Intent(this@ProfilPruefungActivity, MainActivity::class.java)
//                this@ProfilPruefungActivity.startActivity(intent)
//            }
//        })
//    }
//
//    override fun onBackPressed() {
//        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
//            drawer_layout.closeDrawer(GravityCompat.START)
//        } else {
//            super.onBackPressed()
//        }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.template, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        when (item.itemId) {
//            R.id.profil -> {
//                ProfilPressed(this, 0)
//                return true}
//            R.id.messages -> {
//                showKommDialog(this,sp?.getString("vorname","niemand")!!,
//                    sp?.getString("nachname","...")!!,sp?.getInt("id",0)!!,sp?.getInt("auth",0)!!)
//                return true
//            }
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }
//
//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        // Handle navigation view item clicks here.
//        when (item.itemId) {
//            R.id.aktuelles -> {
//                AktuellesPressed(this)
//            }
//            R.id.termine -> {
//                TerminePressed(this)
//            }
//            R.id.kurse -> {
//                KursePressed(this)
//            }
//            R.id.team -> {
//                TeamPressed(this)
//            }
//            R.id.kontakt -> {
//                KontaktPressed(this,sp?.getInt("id",0)!!)
//            }
//            R.id.taekwondo -> {
//                TaekwondoPressed(this)
//            }
//            R.id.kickboxen -> {
//                KickboxenPressed(this)
//            }
//            R.id.pilatis -> {
//                PilatisPressed(this)
//            }
//            R.id.yoga -> {
//                YogaPressed(this)
//            }
//            R.id.nordic -> {
//                NordicWalkingPressed(this)
//            }
//        }
//
//        drawer_layout.closeDrawer(GravityCompat.START)
//        return true
//    }
//}
