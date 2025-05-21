<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="/common/base.jsp">
	<c:param name="title">クラス一覧</c:param>
	<c:param name="scripts"></c:param>
	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">クラス管理</h2>
			<!-- 新規登録リンク -->
			<div class="my-2 text-end px-4">
				<a href="ClassCreate.action" class="btn btn-primary btn-sm">新規登録</a>
			</div>
			<!-- クラス一覧表示 -->
			<c:choose>
				<c:when test="${not empty classList && classList.size() > 0}">
					<table class="table table-hover">
						<thead>
							<tr>
								<th>クラス番号</th>
								<th>学生数</th>
								<th>学校</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="classItem" items="${classList}">
								<tr>
									<td>${classItem.class_num}</td>
									<td>${classItem.c_count}</td>
									<td>${school.name}</td>
									<td><a
										href="ClassUpdate.action?class_num=${classItem.class_num}"
										class="btn btn-sm btn-outline-primary">変更</a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:when>
				<c:otherwise>
					<div class="alert alert-info">クラス情報がありません。</div>
				</c:otherwise>
			</c:choose>
			<div class="mt-3">
				<a href="Menu.action" class="btn btn-secondary">メニューに戻る</a>
			</div>
			<!-- Kuromi画像ボタン -->
			<div class="my-2 text-end px-4">
				<button id="kuromiBtn"
					style="background: none; border: none; padding: 0;">
					<img src="${pageContext.request.contextPath}/image/kuromi_bg.jpg"
						alt="シナモンページへ"
						style="width: 120px; height: auto; cursor: pointer;">
				</button>
			</div>
			<!-- アイキャッチ風全画面動画 -->
			<div id="kuromiModal"
				style="display: none; position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; background-color: black; z-index: 9999; overflow: hidden;">
				<video id="kuromiVideo" autoplay muted playsinline
					style="width: 100%; height: 100%; object-fit: cover;">
					<source src="${pageContext.request.contextPath}/video/kuro.mp4"
						type="video/mp4">
				</video>
			</div>
			<!-- スクリプト -->
			<script>
				// ページロード時に実行
				document.addEventListener('DOMContentLoaded', function() {
					// 前のページからの遷移か確認するためにセッションストレージをチェック
					const returningFromCinnamon = sessionStorage.getItem('returningFromCinnamon');

					// シナモンページから戻ってきた場合は、確実に通常画面表示に戻す
					if (returningFromCinnamon === 'true') {
						sessionStorage.removeItem('returningFromCinnamon');
						hideKuromiModal();
					}

					// 念のため、ページ読み込み時に毎回モーダルを非表示にする
					hideKuromiModal();

					// クルミボタンクリックイベント設定
					document.getElementById('kuromiBtn').addEventListener('click', playKuromiVideo);
				});

				// クルミ動画再生関数
				function playKuromiVideo() {
					const modal = document.getElementById('kuromiModal');
					const video = document.getElementById('kuromiVideo');

					modal.style.display = 'block';
					video.currentTime = 0;
					video.play();

					// 動画再生終了時の処理
					video.onended = function() {
						// シナモンページへ移動する前にセッションストレージにフラグを設定
						sessionStorage.setItem('navigatingToCinnamon', 'true');
						window.location.href = "https://www.sanrio.co.jp/characters/cinnamon/";
					};
				}

				// ページ離脱時の処理
				window.addEventListener('beforeunload', function(e) {
					// シナモンページへ遷移する場合のみ処理
					if (sessionStorage.getItem('navigatingToCinnamon') === 'true') {
						sessionStorage.setItem('returningFromCinnamon', 'true');
						sessionStorage.removeItem('navigatingToCinnamon');
					}
				});

				// ブラウザの戻るボタンの処理も確実にキャッチするため
				window.addEventListener('popstate', function(e) {
					hideKuromiModal();
				});

				// 戻るボタンで戻ってきた場合の処理
				window.addEventListener('pageshow', function(event) {
					// bfcacheから復元された場合も含めて処理
					if (event.persisted || sessionStorage.getItem('returningFromCinnamon') === 'true') {
						sessionStorage.removeItem('returningFromCinnamon');
						hideKuromiModal();
					}
				});

				// モーダルを非表示にする関数
				function hideKuromiModal() {
					const modal = document.getElementById('kuromiModal');
					const video = document.getElementById('kuromiVideo');
					if (modal) {
						modal.style.display = 'none';
					}
					if (video) {
						video.pause();
						video.currentTime = 0;
					}
				}

				// シナモンページへのリンクをクリックした場合
				const links = document.querySelectorAll('a[href^="https://www.sanrio.co.jp/characters/cinnamon/"]');
				links.forEach(function(link) {
					link.addEventListener('click', function(e) {
						e.preventDefault();
						playKuromiVideo();
					});
				});
			</script>
		</section>
	</c:param>
</c:import>