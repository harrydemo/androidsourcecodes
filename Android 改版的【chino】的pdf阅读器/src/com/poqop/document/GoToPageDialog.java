package com.poqop.document;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.poqop.R;

public class GoToPageDialog extends Dialog
{
    private final DocumentView documentView;
    private final DecodeService decodeService;

    public GoToPageDialog(final Context context, final DocumentView documentView, final DecodeService decodeService)
    {
        super(context);
        this.documentView = documentView;
        this.decodeService = decodeService;
        setTitle("跳转至：");
        setContentView(R.layout.gotopage);
        final Button button = (Button) findViewById(R.id.goToButton);
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                goToPageAndDismiss();
            }
        });
        final EditText editText = (EditText) findViewById(R.id.pageNumberTextEdit);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent)
            {
                if (actionId == EditorInfo.IME_NULL || actionId == EditorInfo.IME_ACTION_DONE)
                {
                    goToPageAndDismiss();
                    return true;
                }
                return false;
            }
        });
    }

    private void goToPageAndDismiss()
    {
        navigateToPage();
        dismiss();
    }

    /*
     * 跳转页面，不存在时的提示
     */
    private void navigateToPage()
    {
        final EditText text = (EditText) findViewById(R.id.pageNumberTextEdit);
        final int pageNumber = Integer.parseInt(text.getText().toString());
        if (pageNumber < 1 || pageNumber > decodeService.getPageCount())
        {
            Toast.makeText(getContext(), "该文档只有: 1-" + decodeService.getPageCount(), 2000).show();
            return;
        }
        documentView.goToPage(pageNumber-1);
    }
}
