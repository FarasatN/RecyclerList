//package com.farasatnovruzov.recyclerlist
//
//class NotificationDetailActivity : BaseActivity() {
//
//    lateinit var viewModel: NotificationViewModel
//
//    @Inject
//    lateinit var factory: ViewModelProviderFactory
//    @SuppressLint("SuspiciousIndentation")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_notification_detail)
//
//        val linkDetail = findViewById(R.id.linkDetailText) as TextView
//
//        toolbar_back_button.setOnClickListener { onBackPressed() }
//        val title = intent.getStringExtra("title")
//        val image = intent.getStringExtra("image")
//        val body = intent.getStringExtra("body")
//
//
//        if (hasUrl(body)){
//            var url = extractUrl(body)
//            descDetailText.text = removeUrl(body)
//            linkDetail.visibility  = View.VISIBLE
//            linkDetail.setText(url)
//            linkDetail.movementMethod = LinkMovementMethod.getInstance()
//            linkDetail.setOnClickListener {
//                val browserIntent = Intent(Intent.ACTION_VIEW)
//                    if(!hasValidUrl(url)){
//                        url = "https://"+url
//                        browserIntent.data = Uri.parse(url)
//                        startActivity(browserIntent)
//                    }else {
//                        browserIntent.data = Uri.parse(extractUrl(body))
//                        startActivity(browserIntent)
//                    }
//            }
//        }else{
//            linkDetail.visibility  = View.GONE
//            descDetailText.text = body
//        }
//
//        titleDetailText.text = title
//        toolbar_title.text = getString(R.string.notification)
//
//
//        if (image.length > 0) {
//            Picasso.with(this)
//                .load(image)
//                .fit()
//                .centerCrop()
//                .into(detailNotificationImage)
//        }
//
//        viewModel = ViewModelProvider(this, factory)[NotificationViewModel::class.java]
//    }
//}