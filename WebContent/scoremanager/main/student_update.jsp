```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/common/base.jsp">
	<c:param name="title">学生情報変更</c:param>
	<c:param name="scripts"></c:param>
	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">学生情報変更</h2>

			<form action="StudentUpdateExecute.action" method="post">
				<input type="hidden" name="no" value="${student.no}" />

				<div class="row mb-3">
					<div class="col-12">
						<label class="form-label" for="student-year">入学年度</label>
						<select class="form-select" id="student-year" name="ent_year">
							<c:forEach var="year" items="${ent_year_set}">
								<option value="${year}" <c:if test="${year == student.entYear}">selected</c:if>>${year}</option>
							</c:forEach>
						</select>
					</div>
				</div>

				<div class="row mb-3">
					<div class="col-12">
						<label class="form-label" for="student-no">学生番号</label>
						<input type="text" class="form-control" id="student-no" value="${student.no}" readonly />
					</div>
				</div>

				<div class="row mb-3">
					<div class="col-12">
						<label class="form-label" for="student-name">氏名</label>
						<input type="text" class="form-control" id="student-name" name="name"
							value="${student.name}" maxlength="30" required />
					</div>
				</div>

				<div class="row mb-3">
					<div class="col-12">
						<label class="form-label" for="student-class">クラス</label>
						<select class="form-select" id="student-class" name="class_num">
							<c:forEach var="cls" items="${sclassList}">
								<option value="${cls}" <c:if test="${cls == student.classNum}">selected</c:if>>${cls}</option>
							</c:forEach>
						</select>
					</div>
				</div>

				<div class="row mb-3">
					<div class="col-12">
						<div class="form-check">
							<input class="form-check-input" type="checkbox" id="is-attend" name="is_attend"
								<c:if test="${student.attend}">checked</c:if> />
							<label class="form-check-label" for="is-attend">在学中</label>
						</div>
					</div>
				</div>

				<div class="col-2 text-left mb-3">
					<button class="btn btn-primary" id="end">更新</button>
				</div>

				<div class="row px-4">
					<a href="StudentList.action">戻る</a>
				</div>
			</form>
		</section>
	</c:param>
</c:import>
```