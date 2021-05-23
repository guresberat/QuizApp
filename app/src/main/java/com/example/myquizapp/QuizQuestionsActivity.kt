package com.example.myquizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private var mCurrentPosition: Int = 1
    private var mQuestionsList: ArrayList<Question>? = null
    private var mSelectedOptionPosition: Int = 0
    private var mCorrectAnswers: Int = 0
    private var mUserName: String? = null

    lateinit var progressBar: ProgressBar
    lateinit var tv_progress: TextView
    lateinit var tv_question: TextView
    lateinit var iv_image: ImageView
    lateinit var tv_optionOne: TextView
    lateinit var tv_optionTwo: TextView
    lateinit var tv_optionThree: TextView
    lateinit var tv_optionFour: TextView
    lateinit var btn_submit: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        mUserName = intent.getStringExtra(Constants.USER_NAME)


        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        progressBar = findViewById(R.id.progressBar)
        tv_progress = findViewById(R.id.tv_progress)
        tv_question = findViewById(R.id.tv_question)
        iv_image = findViewById(R.id.iv_image)
        tv_optionOne = findViewById(R.id.tv_option_one)
        tv_optionTwo = findViewById(R.id.tv_option_two)
        tv_optionThree = findViewById(R.id.tv_option_three)
        tv_optionFour = findViewById(R.id.tv_option_four)
        btn_submit = findViewById(R.id.btn_submit)

        mQuestionsList = Constants.getQuestions()

        setQuestion()

        tv_optionOne.setOnClickListener(this@QuizQuestionsActivity)
        tv_optionTwo.setOnClickListener(this@QuizQuestionsActivity)
        tv_optionThree.setOnClickListener(this@QuizQuestionsActivity)
        tv_optionFour.setOnClickListener(this@QuizQuestionsActivity)
        btn_submit.setOnClickListener(this)


    }

    private fun setQuestion() {

        val question = mQuestionsList!![mCurrentPosition - 1]

        defaultOptionsView()

        if(mCurrentPosition == mQuestionsList!!.size){
            btn_submit.text = "FINISH"
        }else{
            btn_submit.text = "SUBMIT"
        }

        progressBar.progress = mCurrentPosition
        tv_progress.text = "$mCurrentPosition" + "/" + progressBar.max

        tv_question.text = question.question
        iv_image.setImageResource(question.image)
        tv_optionOne.text = question.optionOne
        tv_optionTwo.text = question.optionTwo
        tv_optionThree.text = question.optionThree
        tv_optionFour.text = question.optionFour
    }

    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()
        options.add(0, tv_optionOne)
        options.add(1, tv_optionTwo)
        options.add(2, tv_optionThree)
        options.add(3, tv_optionFour)

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this@QuizQuestionsActivity,
                R.drawable.default_option_border_bg
            )
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_option_one -> {
                selectedOptionView(tv_optionOne, 1)
            }
            R.id.tv_option_two -> {
                selectedOptionView(tv_optionTwo, 2)
            }
            R.id.tv_option_three -> {
                selectedOptionView(tv_optionThree, 3)
            }
            R.id.tv_option_four -> {
                selectedOptionView(tv_optionFour, 4)
            }
            R.id.btn_submit -> {
                if (mSelectedOptionPosition == 0) {
                    mCurrentPosition++

                    when {
                        mCurrentPosition <= mQuestionsList!!.size -> {
                            setQuestion()
                        }
                        else -> {
                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, mUserName)
                            intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList!!.size)
                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
                    val question = mQuestionsList?.get(mCurrentPosition - 1)
                    if (question!!.correctAnswer != mSelectedOptionPosition) {
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                    }else{
                        mCorrectAnswers++
                    }
                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)
                    if (mCurrentPosition == mQuestionsList!!.size) {
                        btn_submit.text = "FINISH"
                    } else {
                        btn_submit.text = "Go To Next Questin"
                    }
                    mSelectedOptionPosition = 0
                }
            }
        }
    }

    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {
            1 -> {
                tv_optionOne.background = ContextCompat.getDrawable(this, drawableView)
            }
            2 -> {
                tv_optionTwo.background = ContextCompat.getDrawable(this, drawableView)
            }
            3 -> {
                tv_optionThree.background = ContextCompat.getDrawable(this, drawableView)
            }
            4 -> {
                tv_optionFour.background = ContextCompat.getDrawable(this, drawableView)
            }
        }
    }


    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int) {
        defaultOptionsView()
        mSelectedOptionPosition = selectedOptionNum
        tv.setTextColor(Color.parseColor("#383A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this@QuizQuestionsActivity,
            R.drawable.selected_option_border_bg
        )
    }

}