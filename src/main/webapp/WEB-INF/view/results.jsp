<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; UTF-8" %>
<%@ page import="java.util.*"%>
<%
   // Create global variables
   Object query = request.getAttribute("query");
   int pageNum = (Integer) request.getAttribute("pageNum");
   List<ArrayList<String>> result = (List<ArrayList<String>>) request
   .getAttribute("resultList");
   int maxPages = ((int) result.size() / 10) + 1;
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title><%=query.toString()%> - ${sitename} Search</title>
<link rel="stylesheet" href="../css/styleResult.css" type="text/css" />
<!--[if !IE 7]>
   <style type="text/css">
      #wrap {display:table;height:100%}
   </style>
<![endif]-->
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.1/jquery.min.js"></script>
<script src="../js/jquery-live-preview.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$(".livepreview").livePreview({
			viewWidth : 525,
			viewHeight : 600,
			targetWidth : 1000,
			targetHeight : 800,
			offset : 50,
			position : 'right'
		});
	});
</script>
</head>
<body>
 <div class="top-bar-black" id=BlackBar>
      <a href="javascript: void(0)" style="color:#fff">${search}</a>
      <form name="webSearchTop" method="POST" action="/image/search" style="display:inline">
         <input type="hidden" name="page" value="1"> <input
            type="hidden" value="<%=query.toString()%>" name="search_field"
            id="search_field">
            <a href="javascript:document.webSearchTop.submit()">${images}</a>
      </form>
   </div>
<div id="wrap">
   <div id="main">

	<div class=topbar id=TopBar>
		<a href="/"> <img src="../img/logoMini.png" alt="${sitename}"
                                   width="156" height="38" border="0">
		</a>
		<form class="form-wrapper cf" method="post" name="search_frm"
			action="/web/search"
			onsubmit="if (document.getElementById('search_field').value.length < 1) return false;">
			<input type="text" value="<%=query.toString()%>" name="search_field"
				id="search_field" required> <input type="hidden" name="page"
				value="1">
			<button type="submit" name="submit" value="Search"><img src="../img/search.png"></button>
		</form>
	</div>

	<div class="stub" id=Stub>
	<a href="javascript: void(0)" class="stub-focus">${web}</a>
	<form name="webSearch" method="POST" action="/image/search" style="display:inline">
         <input type="hidden" name="page" value="1"> <input
            type="hidden" value="<%=query.toString()%>" name="search_field"
            id="search_field">
            <a href="javascript:document.webSearch.submit()" class="stub-regular">${images}</a>
      </form>
	 </div>


	<div class=results>
		<%
		   // If no results are found
		   if (result != null)
		   {
		      if (result.size() == 0)
		      {
		%>
		<h5>Your search - <b><%=query.toString()%></b> - did not match any documents. <br><br>
         Suggestions: <br><br>

         &bull; Make sure all words are spelled correctly.<br>
         &bull; Try different keywords.<br>
         &bull; Try more general keywords.<br>
         &bull; Try fewer keywords.
      </h5>

		<%
		   }
		      // Print only 10 entries from the result list
		      else
		      {

		         for (int i = (pageNum - 1) * 10; i < pageNum * 10
		               && i < result.size(); i++)
		         {
		%>
		<a href="<%=result.get(i).get(0)%>" style="color:blue;"><%=result.get(i).get(1)%></a>
		<div class=arrow-right>
			<a href="<%=result.get(i).get(0)%>" target="_blank" onclick="return false"
				class="livepreview">&raquo;</a>
		</div>
		<h4><%=result.get(i).get(0)%></h4>
		<h3><%=result.get(i).get(2)%></h3>
		<br />
		<%
		   }
		      }
		   }
		%>
	</div>
	  </div>
	</div>

</body>

<footer>
<div id="footer">
<div class=pageNumbers>
      <%
         if (maxPages > 1)
         { // Show "Purd"
      %>
      <img class=pageNumberImageFront src="../img/page_num_front.png">
      <% } %>
      <%
         if (pageNum > 1)
         { // show PREVIOUS button if user is not on the first page
      %>
      <form name="pageForm0" method="POST" action="/web/search" class=Prev>
         <input type="hidden" name="page" value="<%=pageNum - 1%>"> <input
            type="hidden" value="<%=query.toString()%>" name="search_field"
            id="search_field"> <a
            href="javascript:document.pageForm0.submit()">Previous</a>
      </form>
      <%
         } // PAGE NUMBERS
         if (maxPages > 1) // If there is more than one page of results
         {
            if(pageNum <= 5) // User is page 5 or less
            {
               // Show pages 1 to 10
               for (int i = 1; i <= 10 && i <= maxPages; i++)
               {
                   String title;
                    if (i == pageNum)
                    {
                         title = "<b><font color=\"000000\">" + i + "</font></b>";
                         %>
                         <img class=pageNumberImage src="../img/page_num_green.png">
                         <%
                    }
                    else
                    {
                        title = i + "";//kl
                        %>
                        <img class=pageNumberImage src="../img/page_num_red.png">
                        <%
                    }
      %>
      <form name="pageForm<%=i%>" method="POST" action="/web/search">
         <input type="hidden" name="page" value="<%=i%>"> <input
            type="hidden" value="<%=query.toString()%>" name="search_field"
            id="search_field"> <a
            href="javascript:document.pageForm<%=i%>.submit()"><%=title%></a>
      </form>
      <%
           } // for
            } //pageNum <=5
            else
               {
                  for (int i = pageNum - 4; i <= pageNum; i++)
                  {
                      String title;
                       if (i == pageNum)
                       {
                            title = "<b><font color=\"000000\">" + i + "</font></b>";
                            %>
                            <img class=pageNumberImage src="../img/page_num_green.png">
                            <%
                       }
                       else
                       {
                           title = i + "";
                           %>
                           <img class=pageNumberImage src="../img/page_num_red.png">
                           <%
                      }
         %>
         <form name="pageForm<%=i%>" method="POST" action="/web/search">
            <input type="hidden" name="page" value="<%=i%>"> <input
               type="hidden" value="<%=query.toString()%>" name="search_field"
               id="search_field"> <a
               href="javascript:document.pageForm<%=i%>.submit()"><%=title%></a>
         </form>
         <%
              } // for (1-5)
              for (int i = pageNum + 1; i < pageNum + 5 && i <= maxPages; i++)
               {
                   String title;
                    if (i == pageNum)
                    {
                         title = "<b><font color=\"000000\">" + i + "</font></b>";
                         %>
                         <img class=pageNumberImage src="../img/page_num_green.png">
                         <%
                    }
                    else
                    {
                        title = i + "";
                        %>
                        <img class=pageNumberImage src="../img/page_num_red.png">
                        <%
                   }
      %>
      <form name="pageForm<%=i%>" method="POST" action="/web/search">
         <input type="hidden" name="page" value="<%=i%>"> <input
            type="hidden" value="<%=query.toString()%>" name="search_field"
            id="search_field"> <a
            href="javascript:document.pageForm<%=i%>.submit()"><%=title%></a>
      </form>
      <%
           } // for (6-10)
               } // else
         } // maxPages > 1
         if(maxPages > 1)
         {
         %>
         <img class=pageNumberImageEnd src="../img/page_num_end.png">
         <%
         }
         if (pageNum < maxPages)
         { // NEXT
      %>
      <form name="pageForm<%=maxPages + 1%>" method="POST" action="/web/search" class=Next>
         <input type="hidden" name="page" value="<%=pageNum + 1%>"> <input
            type="hidden" value="<%=query.toString()%>" name="search_field"
            id="search_field">
            <a href="javascript:document.pageForm<%=maxPages + 1%>.submit()">Next</a>
      </form>

      <%
         }
      %>
        </div>
		<div class=footer>
			<p>Istep Solutions &copy; ${sitename} 2018</p>
		</div>
</div>
</footer>
</html>