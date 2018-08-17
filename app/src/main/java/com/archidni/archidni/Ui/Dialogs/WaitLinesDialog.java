package com.archidni.archidni.Ui.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.archidni.archidni.Model.Path.WaitLine;
import com.archidni.archidni.R;
import com.archidni.archidni.Ui.Adapters.LineInsideWaitInstructionAdapter;

import java.util.ArrayList;

public class WaitLinesDialog extends Dialog {
    private ArrayList<WaitLine> waitLines;
    private LineInsideWaitInstructionAdapter.OnItemClick onItemClick;

    public WaitLinesDialog(@NonNull Context context, ArrayList<WaitLine> waitLines, LineInsideWaitInstructionAdapter.OnItemClick onItemClick) {
        super(context);
        this.waitLines = waitLines;
        this.onItemClick = onItemClick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wait_lines);
        RecyclerView recyclerView = findViewById(R.id.list_wait_lines);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        LineInsideWaitInstructionAdapter lineInsideWaitInstructionAdapter =
                new LineInsideWaitInstructionAdapter(getContext(), waitLines, new LineInsideWaitInstructionAdapter.OnItemClick() {
                    @Override
                    public void onItemClick(WaitLine waitLine) {
                        onItemClick.onItemClick(waitLine);
                    }
                });
        recyclerView.setAdapter(lineInsideWaitInstructionAdapter);
    }
}
