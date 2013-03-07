package com.grep.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;


/**
 * TopicKeywordsDialogFragment allows for the creation of a new topic,
 * with specified keywords, or the editing of a topic's keywords.
 * 
 * @author Gresham, Ryan, Everett, Pierce
 *
 */
public class TopicKeywordsDialogFragment extends DialogFragment {

		public TopicKeywordsDialogFragment() {
			//default constructor, for new topic
		}
		
		public TopicKeywordsDialogFragment(String [] str /* contains all keywords of topic */) {
			//overloaded constructor, for editing existing topic
		}
		
		@Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Build the dialog and set up the button click handlers
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	     
	        // Get the layout inflater
	        LayoutInflater inflater = getActivity().getLayoutInflater();
	        
	        // Get view from inflater
	        final View view = inflater.inflate(R.layout.keyword_dialog, null);
	        
	        // Inflate and set the layout for the dialog
	        // Pass null as the parent view because its going in the dialog layout
	        builder.setMessage("Topic Keywords")
	        	   .setView(view)
	        	   	// Add action buttons
	               .setPositiveButton("Done", new DialogInterface.OnClickListener() {    
	            	   public void onClick(DialogInterface dialog, int id) {
	                    	  //Action performed when done
	                   }
	               })
	               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   TopicKeywordsDialogFragment.this.getDialog().cancel();
	                   }
	               });
	        return builder.create();
	    }
}