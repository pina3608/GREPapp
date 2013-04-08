package com.grep.ui;

import java.util.ArrayList;
import java.util.List;

import com.grep.database.DatabaseHandler;
import com.grep.database.Keyword;
import com.grep.database.Topic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


/**
 * TopicKeywordsDialogFragment allows for the creation of a new topic,
 * with specified keywords, or the editing of a topic's keywords.
 * 
 * @author Gresham, Ryan, Everett, Pierce
 *
 */

//load original database into list (load id's), if I delete a keyword change the id tag to negative of that id
//if I add a keyword, give it an id tag of 0
//if I edit a keyword, leave it's id as it
//in the end, loop through secondary list of keywords
//	if negative, remove keyword from database
//  if 0, add keyword to database
//  if positive, compare with the keyword in the database with that id and see if the text has changed, if so update text
public class TopicKeywordsDialogFragment extends DialogFragment {
	ListView keywordsListView;
	EditText topicTitle;
	static EditText newKeywordEditText;
	static ListItemAdapter adapter;
	static List<ListItem> rows = new ArrayList<ListItem>();
	int topicId;
	List<Keyword> keywords = null;
	boolean isNewTopic;
	DatabaseHandler db;
		
	//default constructor, for new topic
	public TopicKeywordsDialogFragment()
	{
		this.isNewTopic = true;
	}
		
	public TopicKeywordsDialogFragment(int topicId, List<Keyword> keywords) 
	{
		//overloaded constructor, for editing existing topic
		this.topicId = topicId;
		this.keywords = keywords;
		this.isNewTopic = false;
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		db.open();
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		db.close();
	}
		
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		db = new DatabaseHandler(getActivity());
		db.open(); //do I have to call this here as well, was getting null pointer exceptions with database when this wasn't here
				
		//Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
	        
		//Get view from inflater
		final View view = inflater.inflate(R.layout.keyword_dialog, null);
		
		
		//listview of keywords we will populate, edittext for the topic title, edittext for new keywords
		keywordsListView   = (ListView) view.findViewById(R.id.keywordsListView);
		topicTitle         = (EditText) view.findViewById(R.id.topicEditText);
		newKeywordEditText = (EditText) view.findViewById(R.id.newKeywordEditText);
		
		/* 		
	    newKeywordEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

	        @Override
	        public void onFocusChange(View v, boolean hasFocus) {      	
	        	//if we lost focus and there is text in the new keyword edit box, add the keyword to the list
	        	if(!hasFocus && !newKeywordEditText.getText().toString().isEmpty()) {
	        		rows.add(new ListItem(R.drawable.delete_x, newKeywordEditText.getText().toString()));
	        		newKeywordEditText.setText("");
	        		newKeywordEditText.setHintTextColor(getResources().getColor(R.color.black));
	        	}
	        }
	    });
	    
	    */
	    
	    //TODO this is not quite working, need to get this working or figure out a better way of doing
	    //this, maybe a checkmark button or something. Also need to validate that at least one keyword
	    //was given, else highlight new keyword in red and pop a toast
	    //the below works on Jason's phone but listview items don't pop up until exiting keyboard

		/* Use enter key to add keyword in addKeywordEditText to the keyword list, some issues
		 Currently works to use enter to add keyword to list, but only for first two keywords, then has issues

		newKeywordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
	        @Override
	        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	        	//if done button hit on keyboard
	        	if(actionId == EditorInfo.IME_ACTION_DONE) { 
		        	//if there is text in the new keyword edit box, add the keyword to the list
		        	if(!newKeywordEditText.getText().toString().isEmpty()) {
		        		rows.add(new ListItem(R.drawable.delete_x, newKeywordEditText.getText().toString()));
		        		newKeywordEditText.setText("");
		        		newKeywordEditText.setHintTextColor(getResources().getColor(R.color.black));		        		
		        	}
		        	

	                return true;
	            }
	            return false;
	        }
	    });
		 */		
		
		//since rows is static, it may need to be cleared if there were existing keyword rows left over from last view of activity
		rows.clear();
			
		//if existing topic, populate the keywords list with the keywords for this topic
	    if (!isNewTopic) {
	    	topicTitle.setText(db.getTopic(this.topicId).getTopicName());
	    	
			if(keywords != null) { //shouldn't ever be null, but if this is the case, keywords.size() throws exception
				for (int i = 0; i < keywords.size(); i++)
		    	{
		      		rows.add(new ListItem(R.drawable.delete_x, keywords.get(i).getKeyword() + "no!!!", keywords.get(i).getId() )); //TODO do some testing to make sure this keyword id is correct
		    	}
			}		    	
	    }
   
		//create an adapter which defines the data/format of each element of our listview
		adapter = new ListItemAdapter(view.getContext(), R.layout.keywords_item_row, rows, ListItemAdapter.listItemType.KEYWORD);
	       
		//set our adapter for the listview so that we can know what each list element (row) will be like
		keywordsListView.setAdapter(adapter);

		
		/*AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
builder.setMessage("Test for preventing dialog close");
AlertDialog dialog = builder.create();
dialog.show();
//Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
      {            
          @Override
          public void onClick(View v)
          {
              Boolean wantToCloseDialog = false;
              //Do stuff, possibly set wantToCloseDialog to true then...
              if(wantToCloseDialog)
                  dismiss();
              //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
          }
      });
*/
		
		//Build the dialog and set up the button click handlers
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());		
		
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setMessage("Topic Keywords")
			.setView(view)
			
			// Add action buttons
			.setPositiveButton("Save Topic", null)
			.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id)
				{
					TopicKeywordsDialogFragment.this.getDialog().cancel();
				}
			})
			.setNegativeButton("Delete Topic", null);
		
		AlertDialog dialog = builder.create();
		dialog.show();
		
		//set up the action for when the save topic button is clicked
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				//TODO need to do something special depending on if this is a new topic or not? like only saving vs updating
				String topicText = topicTitle.getText().toString();

				if (topicText.isEmpty()) {
					topicTitle.setHintTextColor(getResources().getColor(R.color.red));
					Toast.makeText(view.getContext(), "You need to specify a topic title!", Toast.LENGTH_SHORT).show();
				}
				else {		
					if (rows.isEmpty()) {
						newKeywordEditText.setHintTextColor(getResources().getColor(R.color.red));
						Toast.makeText(view.getContext(), "You must add at least one keyword!", Toast.LENGTH_SHORT).show();
					}	
					else {
						Topic topic = new Topic(topicTitle.getText().toString());
						int topic_id = db.addTopic(topic);
						TopicListActivity.rows.add(new ListItem(R.drawable.edit_pencil, topic.getTopicName(), topic_id));
						TopicListActivity.adapter.notifyDataSetChanged();
						TopicKeywordsDialogFragment.this.getDialog().cancel();			
					}
				}
	         }
		});
		
		//set up the action for when the delete topic button is clicked
		dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				//if not a new topic (we are editing existing topic) we need to actually delete it; if new topic just cancel w/out saving 
				if(!isNewTopic) {
					//TODO should we pop up a warning dialog confirming that they want to delete the topic?
					//delete topic from the database
					db.deleteTopic(topicId);
					
					//delete topic from the topics listview and update the listview
					for(int i = 0; i < TopicListActivity.rows.size(); i++)
					{
						if (TopicListActivity.rows.get(i).getItemId() == topicId) {
							TopicListActivity.rows.remove(i);
							TopicListActivity.adapter.notifyDataSetChanged();
							break;
						}
					}
				}
				
				TopicKeywordsDialogFragment.this.getDialog().cancel();
			}
		});
		
		return dialog;
	}		
}
