<%-- student_list.jsp の完全修正版 - インラインスタイル --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/common/base.jsp">
	<c:param name="title">
        得点管理システム
    </c:param>
	<c:param name="scripts"></c:param>
	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">学生管理</h2>

			<div class="my-2 text-end px-4">
				<a href="StudentCreate.action">新規登録</a>
			</div>

			<form method="get">
				<div class="row border mx-3 mb-3 py-2 align-items-center rounded"
					id="filter">
					<div class="col-4">
						<label class="form-label" for="student-f1-select">入学年度</label> <select
							class="form-select" id="student-f1-select" name="f1">
							<option value="0">--</option>
							<c:forEach var="year" items="${ent_year_set}">
								<option value="${year}"
									<c:if test="${year == f1}">selected</c:if>>${year}</option>
							</c:forEach>
						</select>
					</div>

					<div class="col-4">
						<label class="form-label" for="student-f2-select">クラス</label> <select
							class="form-select" id="student-f2-select" name="f2">
							<option value="0">--</option>
							<c:forEach var="num" items="${class_num_set}">
								<option value="${num}" <c:if test="${num == f2}">selected</c:if>>${num}</option>
							</c:forEach>
						</select>
					</div>

					<div class="col-2 form-check text-center">
						<label class="form-check-label" for="student-f3-check">在学中</label>
						<input class="form-check-input" type="checkbox"
							id="student-f3-check" name="f3" value="1"
							<c:if test="${!empty f3}">checked</c:if> />
					</div>

					<div class="col-2 text-center">
						<button class="btn btn-secondary" id="filter-button">絞込</button>
					</div>
				</div>

				<div class="mt-2 text-warning">${errors.get("f1")}</div>
			</form>
			<c:choose>
				<c:when test="${students.size() > 0}">
					<div>検索結果: ${students.size()}件</div>
					<table style="width: 100%; border-collapse: collapse; margin-bottom: 20px; border: 1px solid #e0e0e0;">
						<tr>
							<th style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; font-weight: normal; background-color: #ffffff;">入学年度</th>
							<th style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; font-weight: normal; background-color: #ffffff;">学生番号</th>
							<th style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; font-weight: normal; background-color: #ffffff;">氏名</th>
							<th style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; font-weight: normal; background-color: #ffffff;">クラス</th>
							<th style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; font-weight: normal; background-color: #ffffff;">在学</th>
							<th style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; font-weight: normal; background-color: #ffffff;"></th>
						</tr>
						<c:forEach var="student" items="${students}">
							<tr>
								<td style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; background-color: #ffffff;">${student.entYear}</td>
								<td style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; background-color: #ffffff;">${student.no}</td>
								<td style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; background-color: #ffffff;">${student.name}</td>
								<td style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; background-color: #ffffff;">${student.classNum}</td>
								<td style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: center; background-color: #ffffff;">
									<c:choose>
										<c:when test="${student.isAttend()}">〇</c:when>
										<c:otherwise>×</c:otherwise>
									</c:choose>
								</td>
								<td style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; background-color: #ffffff;">
									<a href="StudentUpdate.action?no=${student.no}" style="color: #0000ff; text-decoration: none;">編集</a>
								</td>
							</tr>
						</c:forEach>
					</table>
				</c:when>
				<c:otherwise>
					<div>学生情報が存在しませんでした。</div>
				</c:otherwise>
			</c:choose>
		</section>
	</c:param>
</c:import>