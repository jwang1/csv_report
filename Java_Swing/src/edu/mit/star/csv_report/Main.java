package edu.mit.star.csv_report;

import java.awt.Container;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


public class Main extends JFrame
{
	final String LINE_DELIM = "\n";
	final String CSV_DELIM = ",";
	
	private static final long serialVersionUID = 1L;
	JTextArea input;
	JButton calculate;
	JPanel report;
	JTextArea result;

	@Override
	public void addNotify()
	{
		super.addNotify();
		Container c = getContentPane();
		c.setLayout(new BoxLayout(c, BoxLayout.PAGE_AXIS));
		input = new JTextArea(12, 40);
		input.setBorder(BorderFactory.createTitledBorder("Input"));
		calculate = new JButton("Calculate");
		
		// Add event listener
		calculate.addMouseListener(new java.awt.event.MouseAdapter() 
		{
			public void mouseClicked(java.awt.event.MouseEvent evt) 
			{
				onCalculateClicked(evt);
			}
		});
		
		report = new JPanel();
		report.setBorder(BorderFactory.createTitledBorder("Report"));
		result = new JTextArea(12,40);
		report.add(result);
		c.add(input);
		c.add(calculate);
		c.add(report);
	}

    private void onCalculateClicked(java.awt.event.MouseEvent evt) 
    {
    	String txt = input.getText();
    	
    	if(txt == null || "".equals(txt)) 
    	{
    		result.setText("Please Input data above");
    		return;
    	} 
    	
    	//result.setText(input.getText()); return;
    	result.setText(processInput(txt));
    }	
    
    private String processInput(String text) 
    {
    	StringBuilder sb = new StringBuilder();
    	
    	Map<String, Integer> eventTime = null;
    	Map<String, Map<String,Integer>> aveTimeByPerson = parseInput(text);
    	
    	int sum = 0;
    	// calculate results
    	for(Iterator<String> itr = aveTimeByPerson.keySet().iterator(); itr.hasNext(); )
    	{
    		String name = itr.next();
    		
    		eventTime = aveTimeByPerson.get(name);
    		
			sb.append(name);
    		sb.append(":");
    		sb.append(eventTime.size());
    		sb.append(":");
    		
    		sum = 0;
    		for(Map.Entry<String, Integer> entr : eventTime.entrySet())
    		{
    			sum += entr.getValue().intValue();
    		}
    		sb.append(1.0 * sum / eventTime.size() );
    		sb.append("\n");
    		
    	}
    	
    	return sb.toString();
    }
    
    
    private Map<String, Map<String, Integer>> parseInput(String text) {
    	
    	Map<String, Map<String,Integer>> aveTimeByPerson = new HashMap<String, Map<String, Integer>>();
    	
    	Map<String, Integer> eventTime = null;
    
    	// parse text
    	String[] lines = text.split(LINE_DELIM);
    	for(String line : lines) 
    	{
    		//System.out.println(line);
    		String[] elems = line.split(CSV_DELIM);
    		
    		// check the person's name
    		if(!aveTimeByPerson.containsKey(elems[0])) 
    		{
    			eventTime = new HashMap<String, Integer>();
    			aveTimeByPerson.put(elems[0], eventTime);
    		} 
    		else
    		{
    			eventTime = aveTimeByPerson.get(elems[0]);
    		}
    		
    		// check the event and its time
    		if(!eventTime.containsKey(elems[2])) 
    		{
    			eventTime.put(elems[2], Integer.valueOf(elems[1]));
    		} 
    		else 
    		{
    			int sum = Integer.valueOf(elems[1]).intValue() + eventTime.get(elems[2]).intValue();
    			
    			System.out.format("There are duplicate records for Person <%s> at Event <%s>, sum of the time <%d>", 
    								elems[0], elems[1], sum);
    			eventTime.put(elems[2], Integer.valueOf(sum));
    		}
    		
    	}
    	
    	return aveTimeByPerson;
    	
    }
	
    
    
	public static void main(String[] args)
	{
		try
		{
			SwingUtilities.invokeAndWait(new Runnable()
			{

				@Override
				public void run()
				{
					Main m = (new Main());
					m.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					m.pack();
					m.setVisible(true);
				}
			});
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
	}
}
