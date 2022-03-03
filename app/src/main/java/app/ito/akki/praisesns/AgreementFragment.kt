package app.ito.akki.praisesns

import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView


class AgreementFragment : Fragment() {

    private var listener: OnAgreeFragmentListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment = layoutInflater.inflate(R.layout.fragment_agreement,container, false)
        val checkBox = fragment.findViewById<CheckBox>(R.id.checkBox)
        val textView = fragment.findViewById<TextView>(R.id.agreementTextView)
        val toSignUpButton = fragment.findViewById<Button>(R.id.toSignup)
        textView.isVerticalScrollBarEnabled = true
        textView.movementMethod = ScrollingMovementMethod.getInstance()

        toSignUpButton.setOnClickListener {
            if (checkBox.isChecked) {
                listener?.onAgreeFragment()
            }
        }

        return fragment
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnAgreeFragmentListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnHogeFragmentListener")
        }
    }

    interface OnAgreeFragmentListener{
        fun onAgreeFragment()
    }

}